/*
 * Copyright 2012 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package com.gw.monitor.alphaenvmonitor.monitor;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.AttributeKey;

import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Handles a client-side channel.
 */
@Sharable
public class StatusCollectorHandler extends SimpleChannelInboundHandler<String> {

    private static final Logger logger = Logger.getLogger(StatusCollectorHandler.class.getName());

    private long startTime = -1;
    private long endTime = -1;
    
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
//        if (startTime < 0) {
//            startTime = System.currentTimeMillis();
//        }
//        println("Connected to: " + ctx.channel().remoteAddress());
    }

//    private static final AttributeKey<Date> START_TIME =
//            new AttributeKey<Date>("MyHandler.START_TIME");
//
//    private static final AttributeKey<Date> END_TIME =
//            new AttributeKey<Date>("MyHandler.END_TIME");
    
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {  
    	
//    	boolean  isActive = ctx.channel().isActive();
//    	boolean  isOpen = ctx.channel().isOpen();
//    	boolean  isRegistered = ctx.channel().isRegistered();
//    	boolean  isWritable = ctx.channel().isWritable();
    	
//        MyState state = ctx.attr(STATE).get();
//        Date start_Time = ctx.attr(START_TIME).get();
//        Date end_Time = ctx.attr(END_TIME).get();
        
        System.err.println("message from instance:" + msg);
        
//        endTime = System.currentTimeMillis();        
//        long speed = msg.length() /  ((endTime - startTime)/1000);        
//        long speed = msg.length() /  ((end_Time.getTime() - start_Time.getTime())/1000);        
//        System.err.println("speed is" + speed );
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.log(
                Level.WARNING,
                "Unexpected exception from downstream.", cause);
        ctx.close();
    }
}
