# kata.line.message

## APIs

1.  An API receive message from Line webhook, save the user info and message in MongoDB
    > POST /webhook

2. An API query message list of the user from MongoDB
   > GET /messages?userId=U2fa3d682022f56cfdd716945dbe5d19a

   Response
    ```json
    [
      {
        "messageId": "482242971505524741",
        "userId": "U2fa3d682022f56cfdd716945dbe5d19a",
        "text": "Fire",
        "timestampMilli": 1700270514183
      }
    ]
    ```

3. An API send message back to line
   > POST /messages/reply
    
    Request
    ```json
    {
      "messageId": "482243936866533617",
      "text": "Fire ACK"
    } 
   ```
   Response: succeed
   ```json
    {
      "sentMessageId": "482243936866533617",
      "errorMsg": ""
    }
   ```
   Response: error
   ```json
    {
      "sentMessageId": "",
      "errorMsg": "Message Replied."
    }
   ```

## Steps of Test

1. Configuration setup for `Line Message API` and `MongoDB` connection
   
   In dev. environment, you can just override the properties in `src/main/resources/application.yml`

2. Launch up the service using IDE or Maven locally.

3. Launch `ngrok` to forward requests to your local service.
   > ngrok http 8080

   > https://6c9a-111-248-158-77.ngrok.io -> http://localhost:8080
   ![ngrok.png](images%2Fngrok.png)

4. Set up the webhook on `Line Developer Console`
   ![line_console_webhook.png](images%2Fline_console_webhook.png)

5. Scan the QR code with LINE to add your LINE Official Account as a friend.

6. Sent messages to the LINE Official Account channel.
   ![line_sent_msg_to_official_acc.jpg](images%2Fline_sent_msg_to_official_acc.jpg)

7. Check the messages sent are all in Mongo or not.
   ![mongo_msgs.png](images%2Fmongo_msgs.png)

8. Or you can get the messages sending a GET request to the service for a specific user's messages saved in MongoDB.
   ```shell
   curl --location 'localhost:8080/messages?userId=U2fa3d682022f56cfdd716945dbe5d19a'
   ```
   Response
   ```json
   [
    {
        "messageId": "482242971505524741",
        "userId": "U2fa3d682022f56cfdd716945dbe5d19a",
        "text": "Fire",
        "timestampMilli": 1700270514183
    },
    {
        "messageId": "482242982561972354",
        "userId": "U2fa3d682022f56cfdd716945dbe5d19a",
        "text": "Water ",
        "timestampMilli": 1700270520801
    }
   ]
   ```
9. You can reply to a specific message.
   ```shell
   curl --location 'localhost:8080/messages/reply' \
   --header 'Content-Type: application/json' \
   --data '{
   "messageId": "482242971505524741",
   "text": "Fire ACK"}'
   ```
   If succeeded, the response will contain the message id of the reply message.
   ```json
   {
    "sentMessageId": "482243936866533617",
    "errorMsg": ""
   }
   ```

10. Since the `Reply tokens can only be used once`; if reply the message again you will receive:
   ```json
   {
    "sentMessageId": "",
    "errorMsg": "Message Replied."
   }
   ```