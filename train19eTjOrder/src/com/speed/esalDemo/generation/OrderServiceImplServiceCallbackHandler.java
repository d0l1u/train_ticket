
/**
 * OrderServiceImplServiceCallbackHandler.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.4  Built on : Apr 26, 2008 (06:24:30 EDT)
 */

    package com.speed.esalDemo.generation;

    /**
     *  OrderServiceImplServiceCallbackHandler Callback class, Users can extend this class and implement
     *  their own receiveResult and receiveError methods.
     */
    public abstract class OrderServiceImplServiceCallbackHandler{



    protected Object clientData;

    /**
    * User can pass in any object that needs to be accessed once the NonBlocking
    * Web service call is finished and appropriate method of this CallBack is called.
    * @param clientData Object mechanism by which the user can pass in user data
    * that will be avilable at the time this callback is called.
    */
    public OrderServiceImplServiceCallbackHandler(Object clientData){
        this.clientData = clientData;
    }

    /**
    * Please use this constructor if you don't want to set any clientData
    */
    public OrderServiceImplServiceCallbackHandler(){
        this.clientData = null;
    }

    /**
     * Get the client data
     */

     public Object getClientData() {
        return clientData;
     }

        
           /**
            * auto generated Axis2 call back method for checkRateIdPnrValid method
            * override this method for handling normal response from checkRateIdPnrValid operation
            */
           public void receiveResultcheckRateIdPnrValid(
                    com.speed.esalDemo.generation.OrderServiceImplServiceStub.CheckRateIdPnrValidResponseE result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from checkRateIdPnrValid operation
           */
            public void receiveErrorcheckRateIdPnrValid(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for createOrderByPnr method
            * override this method for handling normal response from createOrderByPnr operation
            */
           public void receiveResultcreateOrderByPnr(
                    com.speed.esalDemo.generation.OrderServiceImplServiceStub.CreateOrderByPnrResponseE result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from createOrderByPnr operation
           */
            public void receiveErrorcreateOrderByPnr(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for solveTempNoCanTicket method
            * override this method for handling normal response from solveTempNoCanTicket operation
            */
           public void receiveResultsolveTempNoCanTicket(
                    com.speed.esalDemo.generation.OrderServiceImplServiceStub.SolveTempNoCanTicketResponse1 result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from solveTempNoCanTicket operation
           */
            public void receiveErrorsolveTempNoCanTicket(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for payOrder method
            * override this method for handling normal response from payOrder operation
            */
           public void receiveResultpayOrder(
                    com.speed.esalDemo.generation.OrderServiceImplServiceStub.PayOrderResponse0 result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from payOrder operation
           */
            public void receiveErrorpayOrder(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getOrderDetailInfo method
            * override this method for handling normal response from getOrderDetailInfo operation
            */
           public void receiveResultgetOrderDetailInfo(
                    com.speed.esalDemo.generation.OrderServiceImplServiceStub.GetOrderDetailInfoResponseE result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getOrderDetailInfo operation
           */
            public void receiveErrorgetOrderDetailInfo(java.lang.Exception e) {
            }
                


    }
    