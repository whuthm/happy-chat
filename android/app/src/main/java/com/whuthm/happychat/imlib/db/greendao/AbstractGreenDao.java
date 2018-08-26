package com.whuthm.happychat.imlib.db.greendao;

 class AbstractGreenDao {

     private final GreenDaoOpenHelper openHelper;

     AbstractGreenDao(GreenDaoOpenHelper openHelper) {
         this.openHelper = openHelper;
     }

     GreenDaoOpenHelper getOpenHelper() {
         return openHelper;
     }
 }
