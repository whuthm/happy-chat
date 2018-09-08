package com.whuthm.happychat.imlib;

import com.whuthm.happychat.imlib.dao.IMDaoFactory;

class AbstractChatContextImplService extends AbstractChatService {

     AbstractChatContextImplService(ChatContext chatContext) {
         super(chatContext);
     }

     @Override
     protected ChatContextImpl getChatContext() {
         return (ChatContextImpl) super.getChatContext();
     }

     protected IMDaoFactory getOpenHelper() {
         return getChatContext().getDaoFactory();
     }

     protected EventPoster getEventPoster() {
         return getChatContext().getEventPoster();
     }

 }
