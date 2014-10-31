package com.gw.monitor.alphaenvmonitor.monitor;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.gw.monitor.alphaenvmonitor.domain.ClientStatus;
import com.gw.monitor.alphaenvmonitor.domain.InstanceMonitorStatus;
import com.gw.monitor.alphaenvmonitor.domain.MonitorStatus;
import com.gw.monitor.alphaenvmonitor.domain.ServerStatus;
import com.gw.monitor.alphaenvmonitor.util.BytesUtil;
import com.gw.monitor.alphaenvmonitor.util.FormatTransfer;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;  

@Sharable
public class MonitorServerHandler extends SimpleChannelInboundHandler<String> {

	private static final Logger logger = Logger.getLogger(MonitorServerHandler.class.getName());

    //hostsAndIPs
    
    private final static int PORT_OF_INSTANCE = 8080;
	private final static int PORT_OF_MONITOR = 8777; // 监控端口
	private final static int BYTES_LENGHT_OF_HEAD = 10; // 报头10个字节
    private static final int RECIEVED_CMD_12 = 12;
	private static final int RECIEVED_CMD_13 = 13;
	private static final int RECIEVED_CMD_14 = 14;	
	
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {        
        ctx.write(
                "Welcome to " + InetAddress.getLocalHost().getHostName() + "!\r\n");
        ctx.write("It is " + new Date() + " now.\r\n");
        ctx.flush();
    }
    
    /**  交互协议
         “port”	13		
         “info”	12		
     */
    @Override
    public void channelRead0(ChannelHandlerContext ctx, String request) throws Exception {
        String response = "";
        String body = "";
//        String[] hostInfo = null;
//        String monitorStatus = null;
                        
		byte[] headAndbodyBytes = headAndbodyBytesCreation(request, body);                
        response = new String(headAndbodyBytes,"UTF-8");
                   	                                                  
        ChannelFuture future = ctx.write(response);  

        boolean close = false;
        if (close) {
            future.addListener(ChannelFutureListener.CLOSE);
        }
    }

	private byte[] headAndbodyBytesCreation(String request, String body)
			throws InterruptedException {

		body = bodyCreation(request, body);
		
        byte[] bodyBytes = bodyByteCreation(request, body);  // 先拿到body, 再计算head 
		byte[] headBytes = headByteCreation(body, request); 	
		
        byte[] headAndbodyBytes = mergeHeadAndBody(headBytes, bodyBytes);
		return headAndbodyBytes;
	}

	private byte[] bodyByteCreation(String request, String body) throws InterruptedException {
        byte[] bodyBytes = body.getBytes();
		return bodyBytes;
	}

	private String bodyCreation(String request, String body)
			throws InterruptedException {
		if( request.equalsIgnoreCase("port") ) {   
        	body = monitorStatusOfPort(); 
        }
        
        if( request.equalsIgnoreCase("info") ) {   
        	body = monitorStatusOfInfo(); 
        }
		return body;
	}

	private byte[] mergeHeadAndBody(byte[] headBytes, byte[] bodyBytes) {        
//        int headAndbodyLength = headBytes.length + bodyBytes.length;
//        byte[] headAndbodyBytes = new byte[headAndbodyLength];
       
        byte[] headAndbodyBytes = BytesUtil.byteMerger(headBytes, bodyBytes);
		return headAndbodyBytes;
	}

	private byte[] headByteCreation(String body, String request) {
		byte[] totalLengthByte = totalLengthBytesCreation(body);
        byte[] cmdByte = cmdBytesCreation(request);
//        byte[] seqByte = BytesUtil.intToBytes(0);
//        byte[] seqByte = FormatTransfer.toLH(0); // 低字节开头
        byte[] seqByte = FormatTransfer.toHH(0);
        
        byte[] headByte = headBytesCreation(totalLengthByte, cmdByte, seqByte);
        
        return headByte;
	}

	private byte[] headBytesCreation(byte[] totalLengthByte, byte[] cmdByte,
			byte[] seqByte) {
		byte[] headByte = new byte[10];
        headByte[0] = totalLengthByte[0];
        headByte[1] = totalLengthByte[1];
        headByte[2] = totalLengthByte[2];
        headByte[3] = totalLengthByte[3];
        
        headByte[4] = cmdByte[0];
        headByte[5] = cmdByte[1];
        
        headByte[6] = seqByte[0];
        headByte[7] = seqByte[1];
        headByte[8] = seqByte[2];
        headByte[9] = seqByte[3];
		return headByte;
	}

	private byte[] cmdBytesCreation(String request) {
		short cmd = RECIEVED_CMD_14; // 通用消息 // 默认
        if( request.equalsIgnoreCase("port") ) {
            cmd = RECIEVED_CMD_13;    	
        }
        if( request.equalsIgnoreCase("info") ) {
            cmd = RECIEVED_CMD_12;           	
        }        
//        byte[] cmdByte = BytesUtil.shortToByte(cmd);
//        byte[] cmdByte = FormatTransfer.toLH(cmd); // 低字节开头
        byte[] cmdByte = FormatTransfer.toHH(cmd);
        
		return cmdByte;
	}

	private byte[] totalLengthBytesCreation(String body) {
		int responseLength = 0;
		try {
			responseLength = body.getBytes("UTF-8").length;
		} catch (UnsupportedEncodingException e) {
		}
       
        int totalLength = responseLength + BYTES_LENGHT_OF_HEAD;
//        byte[] totalLengthByte = BytesUtil.intToBytes(totalLength);
//        byte[] totalLengthByte = FormatTransfer.toLH(totalLength);  // 低字节开头
        byte[] totalLengthByte = FormatTransfer.toHH(totalLength);
        
		return totalLengthByte;
	}

	
	
    /**  eg.
  [{"port":1，“type”：“listen”},
  {"port":2，“type”：“query”},
  {"port":3，“type”：“read”}，
  {"port":4，“type”：“write”}]
     */
	private String monitorStatusOfPort() {
		String response;
		//response = "    { {"listen":"123"},{"query":"123"} }";

		Map<String, String> listenMap = new LinkedHashMap<String, String>();  
		listenMap.put( "port", String.valueOf(PORT_OF_INSTANCE) );
		listenMap.put( "type", "listen" );  

		Map<String, String> queryMap = new LinkedHashMap<String, String>();  
		queryMap.put( "port" , String.valueOf(PORT_OF_MONITOR));
		queryMap.put( "type" , "query");
		 		
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();  
        list.add(listenMap);  
        list.add(queryMap); 
        
        JSONArray jsArr = JSONArray.fromObject(list);  
		response = jsArr.toString(4);
		
		return response;
	}


    private Map<String, String> readPropertiesFile() {
//    	List< Map<String, String> > hostIpList = new ArrayList< Map<String, String> >();
    	Map<String, String> hostIpMap = new HashMap<String, String>();
    	
    	InputStream is = this.getClass().getResourceAsStream("hosts.properties");// /hosts.properties
        Properties properties = new Properties();   
        try {
			properties.load(is);
			
			String hostsAndIPs = properties.getProperty("hostsAndIPs");
			String[] hostIPs = hostsAndIPs.split(";");
			for( int i =0; i < hostIPs.length; i++ ) {
				String hostsAndIP  = hostIPs[i];
				String[] hostsAndIPArray =  hostsAndIP.split(":");
				if(hostsAndIPArray.length == 2) {
					hostIpMap.put(hostsAndIPArray[0], hostsAndIPArray[1]);					
				}			
			}				
		} catch (IOException e1) {
			logger.log( Level.SEVERE, " hosts.properties is missing. " );
		}  	 
        
        return hostIpMap;
    }
    
    
	private String monitorStatusOfInfo() throws InterruptedException {		
		///// for test
//    	SocketAddress remoteAddress = new InetSocketAddress("10.15.88.73", 3306);
//    	SocketAddress localAddress = new InetSocketAddress("localhost", 8777);  // 监控接口

        Bootstrap b = new Bootstrap();  
        EventLoopGroup group = new NioEventLoopGroup();         
        b.group(group)
         .channel(NioSocketChannel.class)
         .handler(new StatusCollectorInitializer());
        
		List<ServerStatus> serverStatusListSummary = new ArrayList<ServerStatus>();
				
		Map<String, String> hostIpMap = readPropertiesFile();
		
		for(String remoteHostIp: hostIpMap.keySet()){
			 
		     String remoteHostPort = hostIpMap.get(remoteHostIp); 
		     
//				String remoteHostIp = hostInfo[0];
//				String remoteHostPort = hostInfo[1]; 
					
				InetSocketAddress remoteAddress = new InetSocketAddress(remoteHostIp, Integer.valueOf(remoteHostPort));
				InetSocketAddress localAddress = new InetSocketAddress("localhost", PORT_OF_MONITOR);  
				//monitorPort = monitorPort+1;            
//		        Channel ch = b.connect(host, port).sync().channel();
		            
		        b.bind(localAddress);	            
		        Channel ch = null;
		        List<ServerStatus> serverStatusList;
		        
				try {
					ch = getChannel(b, remoteAddress);
					
			        serverStatusList = serverStatusSetting(ch, remoteAddress);
			        ch.disconnect();  ch.close();
//		            group.register(ch);	  			        
				} catch (ConnectException e) {
					serverStatusList = serverStatusSettingWithConnectRefused(remoteAddress);
				}
		        
				
		        serverStatusListSummary.addAll(serverStatusList);		                  
//	            Channel ch = b.connect(remoteAddress, localAddress).sync().channel();	            
		}  
		
		group.shutdownGracefully();
        
		
            return jsonResponse(serverStatusListSummary);  
//          return jsonResponse(ch, remoteAddress);

//          speedComputation(ch);
	}

	private Channel getChannel(Bootstrap b, InetSocketAddress remoteAddress)
			throws InterruptedException, java.net.ConnectException {
		Channel ch = b.connect(remoteAddress).sync().channel();
		return ch;
	}

	private String jsonResponse(List<ServerStatus> serverStatusListSummary) {
		
        List<ClientStatus> csList = clientStatusSetting();

//        List<ServerStatus> ssList = serverStatusSetting( ch, remoteAddress ); // single
        List<ServerStatus> ssList = serverStatusListSummary; // multi
        
        MonitorStatus ms = monitorStatusSetting(csList, ssList);
        
//        InstanceMonitorStatus ims = instanceMonitorStatusSetting(ms);
//        JSONObject imsJsObj = JSONObject.fromObject(ims);    

        JSONObject imsJsObj = JSONObject.fromObject(ms); 
        
        return imsJsObj.toString(4);
	}

	private InstanceMonitorStatus instanceMonitorStatusSetting(MonitorStatus ms) {
		//
        InstanceMonitorStatus ims = new InstanceMonitorStatus();  
        
////        InetSocketAddress isa = (InetSocketAddress) ch.localAddress();
//        InetSocketAddress isa = new InetSocketAddress("localhost", 8888);
//        String localAddress = isa.getAddress().getHostAddress();
//        int  localPort = isa.getPort();

        InetAddress addr;  //        int  localPort = 8888;
        String localAddress = null;
		try {
			addr = InetAddress.getLocalHost();
	        localAddress = addr.getHostAddress().toString();//获得本机IP	        
		} catch (UnknownHostException e) {
			localAddress = "127.0.0.1";
		}
               
        ims.setIp(localAddress);
        ims.setPort(String.valueOf(PORT_OF_MONITOR));
        ims.setType(1);
        ims.setBuildtime(new Date().toString());
        ims.setInfo(ms);
		return ims;
	}

	private MonitorStatus monitorStatusSetting(List<ClientStatus> csList,
			List<ServerStatus> ssList) {
		MonitorStatus ms = new MonitorStatus();
//        ms.setClients(csList);
//        ms.setServers(ssList);

        ms.setClient(csList);  
        ms.setServer(ssList);
        
		return ms;
	}

	private List<ServerStatus> serverStatusSetting(Channel ch,
			InetSocketAddress remoteAddress) {

        boolean isActive= ch.isActive();
        boolean isOpen= ch.isOpen();
        boolean isRegistered= ch.isRegistered();
        boolean isWritable = ch.isWritable();   
        
//        SocketAddress sa = ch.remoteAddress();  // null
        
//      String  = remoteAddress.getHostName();
        String address= remoteAddress.getAddress().getHostAddress();
        int port = remoteAddress.getPort();
        
		ServerStatus ss = new ServerStatus();
        ss.setServer("ip:port =  " + address +":"+ port);
        ss.setConn(isActive? "ok":"fail");
        ss.setSpeed(0);
        ss.setSlow(0);
        List<ServerStatus>  ssList = new ArrayList<ServerStatus>();       
        ssList.add(ss);
		return ssList;
	}

	private List<ServerStatus> serverStatusSettingWithConnectRefused(
			InetSocketAddress remoteAddress) {

//        boolean isActive= ch.isActive();
//        boolean isOpen= ch.isOpen();
//        boolean isRegistered= ch.isRegistered();
//        boolean isWritable = ch.isWritable();   
        
//        SocketAddress sa = ch.remoteAddress();  // null
        
//      String  = remoteAddress.getHostName();
        String address= remoteAddress.getAddress().getHostAddress();
        int port = remoteAddress.getPort();
        
		ServerStatus ss = new ServerStatus();
        ss.setServer("ip:port =  " + address +":"+ port);
        ss.setConn("fail");
        ss.setSpeed(0);
        ss.setSlow(0);
        List<ServerStatus>  ssList = new ArrayList<ServerStatus>();       
        ssList.add(ss);
		return ssList;
	}
	
	private List<ClientStatus> clientStatusSetting() {
		ClientStatus cs = new ClientStatus();
        cs.setClienttotal(0);
        List<ClientStatus> csList = new ArrayList<ClientStatus>();
        csList.add(cs);
		return csList;
	}


	
	private void speedComputation(Channel ch) throws InterruptedException {
		//            Channel c = ch.read();            
		            ChannelFuture lastWriteFuture = null;
		                        
		            /////
		            String line = "string to request";    
		            lastWriteFuture = ch.writeAndFlush(line + "\r\n");
		            ChannelFuture cf= lastWriteFuture.sync();  
		//            Channel cl = ch.read();
		            ch.closeFuture().sync();
		            /////
		            
		//            ch.attr(END_TIME).set(new Date());
		            
		////            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		////            for (;;) {
		//                String line = "test-test-test";
		////                if (line == null) {
		////                    break;
		////                }
		//                 
		//                // Sends the received line to the server.
		//                lastWriteFuture = ch.writeAndFlush(line + "\r\n");
		//
		//                
		//                // If user typed the 'bye' command, wait until the server closes
		//                // the connection.
		//                if ("bye".equals(line.toLowerCase())) {
		//                    ch.closeFuture().sync();
		////                    break;
		//                }
		////            }
		//                
		//                Channel cl = ch.read();
		//                
		//            // Wait until all messages are flushed before closing the channel.
		////            if (lastWriteFuture != null) {
		////                lastWriteFuture.sync();
		////            }
		//    	/////
	}
	
	
	// socket // test 
	private void method2() throws UnknownHostException, IOException {
//            byte[] bs0 = new byte[] { (byte) 127, (byte) 0, (byte) 0, (byte) 1  };  // ...            
//            InetAddress localHost = InetAddress.getByAddress(bs0);          
            InetAddress localHost = InetAddress.getLocalHost();
            
            byte[] bs1 = new byte[] { (byte) 10, (byte) 15, (byte) 88, (byte) 73  };  // ...
            InetAddress remoteHost = InetAddress.getByAddress(bs1);
            
            Socket socket1 = new Socket(remoteHost, 3306, localHost, 8080);
            boolean isBound= socket1.isBound();
            boolean isClosed= socket1.isClosed();
            boolean isConnected= socket1.isConnected();
            boolean isInputShutdown = socket1.isInputShutdown();
            boolean isOutputShutdown = socket1.isOutputShutdown();   
            
            socket1.close();
	}

	// socket  // test
	private void method3() throws UnknownHostException, IOException {
//		byte[] bs2 = new byte[] { (byte) 127, (byte) 0, (byte) 0, (byte) 1  };  // ...             
//		InetAddress localHost2 = InetAddress.getByAddress(bs2);
          
		InetAddress localHost2 = InetAddress.getLocalHost();
		
		byte[] bs3 = new byte[] { (byte) 10, (byte) 15, (byte) 88, (byte) 333  };  // error address // exception
		InetAddress remoteHost2 = InetAddress.getByAddress(bs3);
		
		Socket socket2 = new Socket(remoteHost2, 3306, localHost2, 8777);
		boolean isBound2= socket2.isBound();
		boolean isClosed2= socket2.isClosed();
		boolean isConnected2= socket2.isConnected();
		boolean isInputShutdown2 = socket2.isInputShutdown();
		boolean isOutputShutdown2 = socket2.isOutputShutdown();

		socket2.close();
	}

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.log(
                Level.WARNING,
                "Unexpected exception from downstream.", cause);
        ctx.close();
    }
}
