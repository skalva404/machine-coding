# **Problem Statement**

### Actor Model ###
    The actor model is a conceptual model to deal with concurrent computation. 
    It defines some general rules for how the system’s components should behave
    and interact with each other.

### Actor ###
    An actor is the primitive unit of computation. It’s the thing that receives 
    a message and do some kind of computation based on it. 
    The idea is very similar to what we have in object-oriented languages:  
    An object receives a message (a method call) and does something depending on 
    which message it receives (which method we are calling).
    
    The main difference is that actors are completely isolated from each other and 
    they will never share memory. It’s also worth noting that an actor can maintain 
    a private state that can never be changed directly by another actor.

### Mailboxes ###
    Messages are sent asynchronously to an actor, that needs to store them somewhere 
    while it’s processing another message. The mailbox is the place where these messages 
    are stored. It’s important to understand that, although multiple actors can run at 
    the same time, an actor will process a given message sequentially.

    Actors communicate with each other by sending asynchronous messages. Those messages are 
    stored in other actors' mailboxes until they're processed.

# **What actors do**
When an actor receives a message, it can do one of these 3 things:
1. Create more actors
2. Send messages to other actors
3. Designate what to do with the next message

Implement a Actor model where user able to instantiate an Actor system and subscribe the actors with their mail boxes,
and producer should be able to produce to the designated mai boxes thus actors shall be able to process the messages received by
their respective mail boxes.
