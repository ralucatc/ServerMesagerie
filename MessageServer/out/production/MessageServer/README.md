This is a messages server that let you send messages to online users, add a message to a specific topic or read all messaged sent to a topic.

1. To send a message using chat option insert the command like this: /chat/destination_user/message
The message should appear to the client which you sent message to (destination_user).

2. To send a message on a topic insert the command like this: /topic/topic_name/timeout/message

3. Any client should be able to see the messages posted on "topic_name" using the following command: /topic/topic_name
The message is going to be deleted after the the mentioned timeout. If the timeout is greater than an hour, the message will be deleted anyway after one hour.