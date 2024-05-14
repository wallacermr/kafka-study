Application to simulate all library flo, since a book reserve until its return.

All of this, using kafka, producer and consumer will be simulate under the modules.

Try to use Ports and Adapters as a architeture to follow.
domain -> core/business_logic that can not be dependency from other layer holding the usecases and the access ir made by ports divided as inputs and outputs
application -> keep the adapter to access the core, make connection to databases, etc, implements the ports
infrastructure -> are the entrances, apis, messages middlewares, etc.

Some tools used:
Docker, Apache Kafka Stream(not kafka cloud streams), mongodb and tools to make trace log.
The desire is to deploy on aws.

