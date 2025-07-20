
## 📦 Kafka Notes

### 🔁 Rebalancing
- When a consumer is added to or removed from a consumer group, Kafka performs a rebalance. This means the partitions of a topic are reassigned among the available consumers.
- For example, if there are 3 partitions for a topic and 2 consumers in the consumer group:
One consumer may be assigned 2 partitions, and the other 1 partition.
If a third consumer joins the group, Kafka will rebalance, and ideally, each consumer will be assigned 1 partition.
- Also, if the number of consumers exceedes the number of the partitions, the extra consumers will remain idle until a rebalance occurs.
### 🔁 Message Ack
- When acks=all, producers consider messages as "written successfully" when the message is accepted by all in-sync replicas (ISR).
- Themin.insync.replicas can be configured both at the topic and the broker-level. The data is considered committed when it is written to all in-sync replicas.
- Request timeout should also be configured. There is also delivery timeout which waits for all operations to end(request, ack, retry)

### 🔁 Retry Mechanism


#### ✅ Retriable Errors

These are errors that **might be resolved after retrying**. For example:

- If the broker returns a `NotEnoughReplicasException`, the producer can retry sending the message.
- In such cases, retrying may succeed after a short time if replica brokers come back online.

#### ❌ Non-Retriable Errors

These are errors that **will not be resolved by retrying**. For example:

- If the broker returns an `INVALID_CONFIG` exception, resending the same producer request will not succeed.

> ⚠️ It's important to handle both types of errors differently in production environments to avoid unnecessary retries or application crashes.

#### Idempotency
- When set to 'true', the producer will ensure that exactly one copy of each message is written in the stream. If 'false', producer retries due to broker failures, etc., may write duplicates of the retried message in the stream. Note that enabling idempotence requires max.in.flight.requests.per.connection to be less than or equal to 5, retries to be greater than 0 and acks must be 'all'. If these values are not explicitly set manually, suitable values will be chosen. If incompatible values are set, a ConfigException will be thrown.
- Kafka adds sequence numbers and a producer ID to each message batch so that when a retry occur, message will be recognized by kafka broker and will not write duplicate messages.
- There is no built in functionality that provides consumer idempotency in Kafka. It needs to be implemented manually.(For example adding unique id to message header on producer side.)

## 🔁 Apache Avro & Schema Registry

**Apache Avro** is a language-independent, schema-based data serialization library. It uses a predefined schema to serialize and deserialize data. Avro schemas are defined using **JSON**, while the data itself is encoded in a **compact binary format**, making it efficient for transmission and storage.

### ✅ Key Benefits of Avro
- 🔹 **Compact**: Binary encoding reduces data size.
- 🔹 **Schema-based**: Ensures consistency between producers and consumers.
- 🔹 **Language-agnostic**: Supports multiple programming languages.
- 🔹 **Type-safe**: Enforces data types at serialization/deserialization time.
- 🔹 **Supports schema evolution**: Handles backward and forward compatibility.

---

### 🗂️ Schema Registry

A **Schema Registry** is a centralized service that stores and manages Avro schemas. It enables producers and consumers to **agree on a common schema** when exchanging data through Kafka.

### ⚙️ How It Works
1. A schema is registered in the Schema Registry and assigned a unique **schema ID**.
2. When a producer sends a message to Kafka, it:
    - Serializes the data using Avro.
    - Embeds the **schema ID** in the message payload.
3. The consumer:
    - Extracts the schema ID from the message.
    - Retrieves the schema from the registry.
    - Deserializes the data using that schema.

### 🗂️ Kafka Transactions
Transactions enable atomic writes to multiple Kafka topics and partitions. All of the messages included in the transaction will be successfully written or none of them will be(Simply like a db transaction)

spring.kafka.consumer.isolation-level=READ_COMMITTED
By default its value is read_uncommited.

The main tradeoff when increasing the transaction duration is that it increases end-to-end latency as it adds additional RPCs for transaction coordinator.