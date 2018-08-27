package com.whuthm.happychat.imlib;

import com.whuthm.happychat.imlib.db.IOpenHelper;

class AbstractChatContextImplService<T> extends AbstractChatService<T> {

     AbstractChatContextImplService(ChatContext chatContext) {
         super(chatContext);
     }

     @Override
     protected ChatContextImpl getChatContext() {
         return (ChatContextImpl) super.getChatContext();
     }

     protected IOpenHelper getOpenHelper() {
         return getChatContext().getOpenHelper();
     }
 }
