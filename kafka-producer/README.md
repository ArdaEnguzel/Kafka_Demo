
## 📦 Kafka Notes

### 🔁 Message Ack
- When acks=all, producers consider messages as "written successfully" when the message is accepted by all in-sync replicas (ISR).
- Themin.insync.replicas can be configured both at the topic and the broker-level. The data is considered committed when it is written to all in-sync replicas.
- Request timeout should also be configured. There is also delivery timeout which waits for all operations to end(request, ack, retry)

### 🔁 Retry Mechanism

Kafka distinguishes between **retriable** and **non-retriable** errors when handling failures:

#### ✅ Retriable Errors

These are errors that **might be resolved after retrying**. For example:

- If the broker returns a `NotEnoughReplicasException`, the producer can retry sending the message.
- In such cases, retrying may succeed after a short time if replica brokers come back online.

#### ❌ Non-Retriable Errors

These are errors that **will not be resolved by retrying**. For example:

- If the broker returns an `INVALID_CONFIG` exception, resending the same producer request will not succeed.

> ⚠️ It's important to handle both types of errors differently in production environments to avoid unnecessary retries or application crashes.

#### Idempotency
- When set to 'true', the producer will ensure that exactly one copy of each message is written in the stream. If 'false', producer retries due to broker failures, etc., may write duplicates of the retried message in the stream. Note that enabling idempotence requires max.in.flight.requests.per.connection to be less than or equal to 5, retries to be greater than 0 and acks must be 'all'. If these values are not explicitly set by the user, suitable values will be chosen. If incompatible values are set, a ConfigException will be thrown.
- Kafka adds sequence numbers and a producer ID to each message batch so that when a retry occur, message will be recognized by kafka broker and will not write duplicate messages.