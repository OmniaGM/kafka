package org.apache.kafka.server.config;

import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.common.GroupType;
import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.common.config.SaslConfigs;
import org.apache.kafka.common.config.SecurityConfig;
import org.apache.kafka.common.config.SslConfigs;
import org.apache.kafka.common.config.TopicConfig;
import org.apache.kafka.common.config.internals.BrokerSecurityConfigs;
import org.apache.kafka.common.record.LegacyRecord;
import org.apache.kafka.common.record.Records;
import org.apache.kafka.common.security.auth.SecurityProtocol;
import org.apache.kafka.common.utils.Utils;
import org.apache.kafka.raft.RaftConfig;
import org.apache.kafka.security.PasswordEncoderConfigs;
import org.apache.kafka.server.common.MetadataVersionValidator;
import org.apache.kafka.server.record.BrokerCompressionType;
import org.apache.kafka.storage.internals.log.LogConfig;
import org.apache.kafka.storage.internals.log.ProducerStateManagerConfig;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static org.apache.kafka.common.config.ConfigDef.Importance.HIGH;
import static org.apache.kafka.common.config.ConfigDef.Importance.LOW;
import static org.apache.kafka.common.config.ConfigDef.Importance.MEDIUM;
import static org.apache.kafka.common.config.ConfigDef.Range.atLeast;
import static org.apache.kafka.common.config.ConfigDef.Range.between;
import static org.apache.kafka.common.config.ConfigDef.Type.CLASS;
import static org.apache.kafka.common.config.ConfigDef.Type.SHORT;
import static org.apache.kafka.common.config.ConfigDef.ValidList;
import static org.apache.kafka.common.config.ConfigDef.ValidString;
import static org.apache.kafka.common.config.ConfigDef.Type.BOOLEAN;
import static org.apache.kafka.common.config.ConfigDef.Type.DOUBLE;
import static org.apache.kafka.common.config.ConfigDef.Type.INT;
import static org.apache.kafka.common.config.ConfigDef.Type.LIST;
import static org.apache.kafka.common.config.ConfigDef.Type.LONG;
import static org.apache.kafka.common.config.ConfigDef.Type.PASSWORD;
import static org.apache.kafka.common.config.ConfigDef.Type.STRING;

public class KafkaConfig {
    private static String LogConfigPrefix = "log.";

        /** ********* Zookeeper Configuration ***********/
        public static String ZkConnectProp = "zookeeper.connect";
        public static String ZkSessionTimeoutMsProp = "zookeeper.session.timeout.ms";
        public static String ZkConnectionTimeoutMsProp = "zookeeper.connection.timeout.ms";
        public static String ZkEnableSecureAclsProp = "zookeeper.set.acl";
        public static String ZkMaxInFlightRequestsProp = "zookeeper.max.in.flight.requests";
        public static String ZkSslClientEnableProp = "zookeeper.ssl.client.enable";
        public static String ZkClientCnxnSocketProp = "zookeeper.clientCnxnSocket";
        public static String ZkSslKeyStoreLocationProp = "zookeeper.ssl.keystore.location";
        public static String ZkSslKeyStorePasswordProp = "zookeeper.ssl.keystore.password";
        public static String ZkSslKeyStoreTypeProp = "zookeeper.ssl.keystore.type";
        public static String ZkSslTrustStoreLocationProp = "zookeeper.ssl.truststore.location";
        public static String ZkSslTrustStorePasswordProp = "zookeeper.ssl.truststore.password";
        public static String ZkSslTrustStoreTypeProp = "zookeeper.ssl.truststore.type";
        public static String ZkSslProtocolProp = "zookeeper.ssl.protocol";
        public static String ZkSslEnabledProtocolsProp = "zookeeper.ssl.enabled.protocols";
        public static String ZkSslCipherSuitesProp = "zookeeper.ssl.cipher.suites";
        public static String ZkSslEndpointIdentificationAlgorithmProp = "zookeeper.ssl.endpoint.identification.algorithm";
        public static String ZkSslCrlEnableProp = "zookeeper.ssl.crl.enable";
        public static String ZkSslOcspEnableProp = "zookeeper.ssl.ocsp.enable";

// a map from the Kafka config to the corresponding ZooKeeper Java system property
    public static final Map<String, String> ZkSslConfigToSystemPropertyMap = Utils.mkMap(
        Utils.mkEntry(ZkSslClientEnableProp, ZKClientConfig.SECURE_CLIENT),
        Utils.mkEntry(ZkClientCnxnSocketProp, ZKClientConfig.ZOOKEEPER_CLIENT_CNXN_SOCKET),
        Utils.mkEntry(ZkSslKeyStoreLocationProp, "zookeeper.ssl.keyStore.location"),
        Utils.mkEntry(ZkSslKeyStorePasswordProp, "zookeeper.ssl.keyStore.password"),
        Utils.mkEntry(ZkSslKeyStoreTypeProp, "zookeeper.ssl.keyStore.type"),
        Utils.mkEntry(ZkSslTrustStoreLocationProp, "zookeeper.ssl.trustStore.location"),
        Utils.mkEntry(ZkSslTrustStorePasswordProp, "zookeeper.ssl.trustStore.password"),
        Utils.mkEntry(ZkSslTrustStoreTypeProp, "zookeeper.ssl.trustStore.type"),
        Utils.mkEntry(ZkSslProtocolProp, "zookeeper.ssl.protocol"),
        Utils.mkEntry(ZkSslEnabledProtocolsProp, "zookeeper.ssl.enabledProtocols"),
        Utils.mkEntry(ZkSslCipherSuitesProp, "zookeeper.ssl.ciphersuites"),
        Utils.mkEntry(ZkSslEndpointIdentificationAlgorithmProp, "zookeeper.ssl.hostnameVerification"),
        Utils.mkEntry(ZkSslCrlEnableProp, "zookeeper.ssl.crl"),
        Utils.mkEntry(ZkSslOcspEnableProp, "zookeeper.ssl.ocsp"));

        /** ********* General Configuration ***********/
        public static String BrokerIdGenerationEnableProp = "broker.id.generation.enable";
        public static String MaxReservedBrokerIdProp = "reserved.broker.max.id";
        public static String BrokerIdProp = "broker.id";
        public static String MessageMaxBytesProp = "message.max.bytes";
        public static String NumNetworkThreadsProp = "num.network.threads";
        public static String NumIoThreadsProp = "num.io.threads";
        public static String BackgroundThreadsProp = "background.threads";
        public static String NumReplicaAlterLogDirsThreadsProp = "num.replica.alter.log.dirs.threads";
        public static String QueuedMaxRequestsProp = "queued.max.requests";
        public static String QueuedMaxBytesProp = "queued.max.request.bytes";
        public static String RequestTimeoutMsProp = CommonClientConfigs.REQUEST_TIMEOUT_MS_CONFIG;
        public static String ConnectionSetupTimeoutMsProp = CommonClientConfigs.SOCKET_CONNECTION_SETUP_TIMEOUT_MS_CONFIG;
        public static String ConnectionSetupTimeoutMaxMsProp = CommonClientConfigs.SOCKET_CONNECTION_SETUP_TIMEOUT_MAX_MS_CONFIG;

        /** KRaft mode configs */
        public static String ProcessRolesProp = "process.roles";
        public static String InitialBrokerRegistrationTimeoutMsProp = "initial.broker.registration.timeout.ms";
        public static String BrokerHeartbeatIntervalMsProp = "broker.heartbeat.interval.ms";
        public static String BrokerSessionTimeoutMsProp = "broker.session.timeout.ms";
        public static String NodeIdProp = "node.id";
        public static String MetadataLogDirProp = "metadata.log.dir";
        public static String MetadataSnapshotMaxNewRecordBytesProp = "metadata.log.max.record.bytes.between.snapshots";
        public static String MetadataSnapshotMaxIntervalMsProp = "metadata.log.max.snapshot.interval.ms";
        public static String ControllerListenerNamesProp = "controller.listener.names";
        public static String SaslMechanismControllerProtocolProp = "sasl.mechanism.controller.protocol";
        public static String MetadataLogSegmentMinBytesProp = "metadata.log.segment.min.bytes";
        public static String MetadataLogSegmentBytesProp = "metadata.log.segment.bytes";
        public static String MetadataLogSegmentMillisProp = "metadata.log.segment.ms";
        public static String MetadataMaxRetentionBytesProp = "metadata.max.retention.bytes";
        public static String MetadataMaxRetentionMillisProp = "metadata.max.retention.ms";
        public static String QuorumVotersProp = RaftConfig.QUORUM_VOTERS_CONFIG;
        public static String MetadataMaxIdleIntervalMsProp = "metadata.max.idle.interval.ms";
        public static String ServerMaxStartupTimeMsProp = "server.max.startup.time.ms";

        /** ZK to KRaft Migration configs */
        public static String MigrationEnabledProp = "zookeeper.metadata.migration.enable";

        /** Enable eligible leader replicas configs */
        public static String ElrEnabledProp = "eligible.leader.replicas.enable";

        /************* Authorizer Configuration ***********/
        public static String AuthorizerClassNameProp = "authorizer.class.name";
        public static String EarlyStartListenersProp = "early.start.listeners";

        /** ********* Socket Server Configuration ***********/
        public static String ListenersProp = "listeners";
        public static String AdvertisedListenersProp = "advertised.listeners";
        public static String ListenerSecurityProtocolMapProp = "listener.security.protocol.map";
        public static String ControlPlaneListenerNameProp = "control.plane.listener.name";
        public static String SocketSendBufferBytesProp = "socket.send.buffer.bytes";
        public static String SocketReceiveBufferBytesProp = "socket.receive.buffer.bytes";
        public static String SocketRequestMaxBytesProp = "socket.request.max.bytes";
        public static String SocketListenBacklogSizeProp = "socket.listen.backlog.size";
        public static String MaxConnectionsPerIpProp = "max.connections.per.ip";
        public static String MaxConnectionsPerIpOverridesProp = "max.connections.per.ip.overrides";
        public static String MaxConnectionsProp = "max.connections";
        public static String MaxConnectionCreationRateProp = "max.connection.creation.rate";
        public static String ConnectionsMaxIdleMsProp = "connections.max.idle.ms";
        public static String FailedAuthenticationDelayMsProp = "connection.failed.authentication.delay.ms";
        /***************** rack configuration *************/
        public static String RackProp = "broker.rack";
        /** ********* Log Configuration ***********/
        public static String NumPartitionsProp = "num.partitions";
        public static String LogDirsProp = LogConfigPrefix + "dirs";
        public static String LogDirProp = LogConfigPrefix + "dir";
        public static String LogSegmentBytesProp = ServerTopicConfigSynonyms.serverSynonym(TopicConfig.SEGMENT_BYTES_CONFIG);

        public static String LogRollTimeMillisProp = ServerTopicConfigSynonyms.serverSynonym(TopicConfig.SEGMENT_MS_CONFIG);
        public static String LogRollTimeHoursProp = LogConfigPrefix + "roll.hours";

        public static String LogRollTimeJitterMillisProp = ServerTopicConfigSynonyms.serverSynonym(TopicConfig.SEGMENT_JITTER_MS_CONFIG);
        public static String LogRollTimeJitterHoursProp = LogConfigPrefix + "roll.jitter.hours";

        public static String LogRetentionTimeMillisProp = ServerTopicConfigSynonyms.serverSynonym(TopicConfig.RETENTION_MS_CONFIG);
        public static String LogRetentionTimeMinutesProp = LogConfigPrefix + "retention.minutes";
        public static String LogRetentionTimeHoursProp = LogConfigPrefix + "retention.hours";

        public static String LogRetentionBytesProp = ServerTopicConfigSynonyms.serverSynonym(TopicConfig.RETENTION_BYTES_CONFIG);
        public static String LogCleanupIntervalMsProp = LogConfigPrefix + "retention.check.interval.ms";
        public static String LogCleanupPolicyProp = ServerTopicConfigSynonyms.serverSynonym(TopicConfig.CLEANUP_POLICY_CONFIG);
        public static String LogCleanerThreadsProp = LogConfigPrefix + "cleaner.threads";
        public static String LogCleanerIoMaxBytesPerSecondProp = LogConfigPrefix + "cleaner.io.max.bytes.per.second";
        public static String LogCleanerDedupeBufferSizeProp = LogConfigPrefix + "cleaner.dedupe.buffer.size";
        public static String LogCleanerIoBufferSizeProp = LogConfigPrefix + "cleaner.io.buffer.size";
        public static String LogCleanerDedupeBufferLoadFactorProp = LogConfigPrefix + "cleaner.io.buffer.load.factor";
        public static String LogCleanerBackoffMsProp = LogConfigPrefix + "cleaner.backoff.ms";
        public static String LogCleanerMinCleanRatioProp = ServerTopicConfigSynonyms.serverSynonym(TopicConfig.MIN_CLEANABLE_DIRTY_RATIO_CONFIG);
        public static String LogCleanerEnableProp = LogConfigPrefix + "cleaner.enable";
        public static String LogCleanerDeleteRetentionMsProp = ServerTopicConfigSynonyms.serverSynonym(TopicConfig.DELETE_RETENTION_MS_CONFIG);
        public static String LogCleanerMinCompactionLagMsProp = ServerTopicConfigSynonyms.serverSynonym(TopicConfig.MIN_COMPACTION_LAG_MS_CONFIG);
        public static String LogCleanerMaxCompactionLagMsProp = ServerTopicConfigSynonyms.serverSynonym(TopicConfig.MAX_COMPACTION_LAG_MS_CONFIG);
        public static String LogIndexSizeMaxBytesProp = ServerTopicConfigSynonyms.serverSynonym(TopicConfig.SEGMENT_INDEX_BYTES_CONFIG);
        public static String LogIndexIntervalBytesProp = ServerTopicConfigSynonyms.serverSynonym(TopicConfig.INDEX_INTERVAL_BYTES_CONFIG);
        public static String LogFlushIntervalMessagesProp = ServerTopicConfigSynonyms.serverSynonym(TopicConfig.FLUSH_MESSAGES_INTERVAL_CONFIG);
        public static String LogDeleteDelayMsProp = ServerTopicConfigSynonyms.serverSynonym(TopicConfig.FILE_DELETE_DELAY_MS_CONFIG);
        public static String LogFlushSchedulerIntervalMsProp = LogConfigPrefix + "flush.scheduler.interval.ms";
        public static String LogFlushIntervalMsProp = ServerTopicConfigSynonyms.serverSynonym(TopicConfig.FLUSH_MS_CONFIG);
        public static String LogFlushOffsetCheckpointIntervalMsProp = LogConfigPrefix + "flush.offset.checkpoint.interval.ms";
        public static String LogFlushStartOffsetCheckpointIntervalMsProp = LogConfigPrefix + "flush.start.offset.checkpoint.interval.ms";
        public static String LogPreAllocateProp = ServerTopicConfigSynonyms.serverSynonym(TopicConfig.PREALLOCATE_CONFIG);

/* See `TopicConfig.MESSAGE_FORMAT_VERSION_CONFIG` for details */
@deprecated("3.0")
  String LogMessageFormatVersionProp = ServerTopicConfigSynonyms.serverSynonym(TopicConfig.MESSAGE_FORMAT_VERSION_CONFIG);

          public static String LogMessageTimestampTypeProp = ServerTopicConfigSynonyms.serverSynonym(TopicConfig.MESSAGE_TIMESTAMP_TYPE_CONFIG);

/* See `TopicConfig.MESSAGE_TIMESTAMP_DIFFERENCE_MAX_MS_CONFIG` for details */
@deprecated("3.6")
  String LogMessageTimestampDifferenceMaxMsProp = ServerTopicConfigSynonyms.serverSynonym(TopicConfig.MESSAGE_TIMESTAMP_DIFFERENCE_MAX_MS_CONFIG);

          public static String LogMessageTimestampBeforeMaxMsProp = ServerTopicConfigSynonyms.serverSynonym(TopicConfig.MESSAGE_TIMESTAMP_BEFORE_MAX_MS_CONFIG);
          public static String LogMessageTimestampAfterMaxMsProp = ServerTopicConfigSynonyms.serverSynonym(TopicConfig.MESSAGE_TIMESTAMP_AFTER_MAX_MS_CONFIG);

          public static String NumRecoveryThreadsPerDataDirProp = "num.recovery.threads.per.data.dir";
          public static String AutoCreateTopicsEnableProp = "auto.create.topics.enable";
          public static String MinInSyncReplicasProp = ServerTopicConfigSynonyms.serverSynonym(TopicConfig.MIN_IN_SYNC_REPLICAS_CONFIG);
          public static String CreateTopicPolicyClassNameProp = "create.topic.policy.class.name";
          public static String AlterConfigPolicyClassNameProp = "alter.config.policy.class.name";
          public static String LogMessageDownConversionEnableProp = ServerTopicConfigSynonyms.serverSynonym(TopicConfig.MESSAGE_DOWNCONVERSION_ENABLE_CONFIG);
          /** ********* Replication configuration ***********/
          public static String ControllerSocketTimeoutMsProp = "controller.socket.timeout.ms";
          public static String DefaultReplicationFactorProp = "default.replication.factor";
          public static String ReplicaLagTimeMaxMsProp = "replica.lag.time.max.ms";
          public static String ReplicaSocketTimeoutMsProp = "replica.socket.timeout.ms";
          public static String ReplicaSocketReceiveBufferBytesProp = "replica.socket.receive.buffer.bytes";
          public static String ReplicaFetchMaxBytesProp = "replica.fetch.max.bytes";
          public static String ReplicaFetchWaitMaxMsProp = "replica.fetch.wait.max.ms";
          public static String ReplicaFetchMinBytesProp = "replica.fetch.min.bytes";
          public static String ReplicaFetchResponseMaxBytesProp = "replica.fetch.response.max.bytes";
          public static String ReplicaFetchBackoffMsProp = "replica.fetch.backoff.ms";
          public static String NumReplicaFetchersProp = "num.replica.fetchers";
          public static String ReplicaHighWatermarkCheckpointIntervalMsProp = "replica.high.watermark.checkpoint.interval.ms";
          public static String FetchPurgatoryPurgeIntervalRequestsProp = "fetch.purgatory.purge.interval.requests";
          public static String ProducerPurgatoryPurgeIntervalRequestsProp = "producer.purgatory.purge.interval.requests";
          public static String DeleteRecordsPurgatoryPurgeIntervalRequestsProp = "delete.records.purgatory.purge.interval.requests";
          public static String AutoLeaderRebalanceEnableProp = "auto.leader.rebalance.enable";
          public static String LeaderImbalancePerBrokerPercentageProp = "leader.imbalance.per.broker.percentage";
          public static String LeaderImbalanceCheckIntervalSecondsProp = "leader.imbalance.check.interval.seconds";
          public static String UncleanLeaderElectionEnableProp = ServerTopicConfigSynonyms.serverSynonym(TopicConfig.UNCLEAN_LEADER_ELECTION_ENABLE_CONFIG);
          public static String InterBrokerSecurityProtocolProp = "security.inter.broker.protocol";
          public static String InterBrokerProtocolVersionProp = "inter.broker.protocol.version";
          public static String InterBrokerListenerNameProp = "inter.broker.listener.name";
          public static String ReplicaSelectorClassProp = "replica.selector.class";
          /** ********* Controlled shutdown configuration ***********/
          public static String ControlledShutdownMaxRetriesProp = "controlled.shutdown.max.retries";
          public static String ControlledShutdownRetryBackoffMsProp = "controlled.shutdown.retry.backoff.ms";
          public static String ControlledShutdownEnableProp = "controlled.shutdown.enable";

          /** ********* Group coordinator configuration ***********/
          public static String GroupMinSessionTimeoutMsProp = "group.min.session.timeout.ms";
          public static String GroupMaxSessionTimeoutMsProp = "group.max.session.timeout.ms";
          public static String GroupInitialRebalanceDelayMsProp = "group.initial.rebalance.delay.ms";
          public static String GroupMaxSizeProp = "group.max.size";

          /** New group coordinator configs */
          public static String NewGroupCoordinatorEnableProp = "group.coordinator.new.enable";
          public static String GroupCoordinatorRebalanceProtocolsProp = "group.coordinator.rebalance.protocols";
          public static String GroupCoordinatorNumThreadsProp = "group.coordinator.threads";

          /** Consumer group configs */
          public static String ConsumerGroupSessionTimeoutMsProp = "group.consumer.session.timeout.ms";
          public static String ConsumerGroupMinSessionTimeoutMsProp = "group.consumer.min.session.timeout.ms";
          public static String ConsumerGroupMaxSessionTimeoutMsProp = "group.consumer.max.session.timeout.ms";
          public static String ConsumerGroupHeartbeatIntervalMsProp = "group.consumer.heartbeat.interval.ms";
          public static String ConsumerGroupMinHeartbeatIntervalMsProp = "group.consumer.min.heartbeat.interval.ms";
          public static String ConsumerGroupMaxHeartbeatIntervalMsProp ="group.consumer.max.heartbeat.interval.ms";
          public static String ConsumerGroupMaxSizeProp = "group.consumer.max.size";
          public static String ConsumerGroupAssignorsProp = "group.consumer.assignors";

          /** ********* Offset management configuration ***********/
          public static String OffsetMetadataMaxSizeProp = "offset.metadata.max.bytes";
          public static String OffsetsLoadBufferSizeProp = "offsets.load.buffer.size";
          public static String OffsetsTopicReplicationFactorProp = "offsets.topic.replication.factor";
          public static String OffsetsTopicPartitionsProp = "offsets.topic.num.partitions";
          public static String OffsetsTopicSegmentBytesProp = "offsets.topic.segment.bytes";
          public static String OffsetsTopicCompressionCodecProp = "offsets.topic.compression.codec";
          public static String OffsetsRetentionMinutesProp = "offsets.retention.minutes";
          public static String OffsetsRetentionCheckIntervalMsProp = "offsets.retention.check.interval.ms";
          public static String OffsetCommitTimeoutMsProp = "offsets.commit.timeout.ms";
          public static String OffsetCommitRequiredAcksProp = "offsets.commit.required.acks";

          /** ********* Transaction management configuration ***********/
          public static String TransactionalIdExpirationMsProp = "transactional.id.expiration.ms";
          public static String TransactionsMaxTimeoutMsProp = "transaction.max.timeout.ms";
          public static String TransactionsTopicMinISRProp = "transaction.state.log.min.isr";
          public static String TransactionsLoadBufferSizeProp = "transaction.state.log.load.buffer.size";
          public static String TransactionsTopicPartitionsProp = "transaction.state.log.num.partitions";
          public static String TransactionsTopicSegmentBytesProp = "transaction.state.log.segment.bytes";
          public static String TransactionsTopicReplicationFactorProp = "transaction.state.log.replication.factor";
          public static String TransactionsAbortTimedOutTransactionCleanupIntervalMsProp = "transaction.abort.timed.out.transaction.cleanup.interval.ms";
          public static String TransactionsRemoveExpiredTransactionalIdCleanupIntervalMsProp = "transaction.remove.expired.transaction.cleanup.interval.ms";

          public static String TransactionPartitionVerificationEnableProp = "transaction.partition.verification.enable";

          public static String ProducerIdExpirationMsProp = ProducerStateManagerConfig.PRODUCER_ID_EXPIRATION_MS;
          public static String ProducerIdExpirationCheckIntervalMsProp = "producer.id.expiration.check.interval.ms";

          /** ********* Fetch Configuration **************/
          public static String MaxIncrementalFetchSessionCacheSlots = "max.incremental.fetch.session.cache.slots";
          public static String FetchMaxBytes = "fetch.max.bytes";

          /** ********* Request Limit Configuration **************/
          public static String MaxRequestPartitionSizeLimit = "max.request.partition.size.limit";

          /** ********* Quota Configuration ***********/
          public static String NumQuotaSamplesProp = "quota.window.num";
          public static String NumReplicationQuotaSamplesProp = "replication.quota.window.num";
          public static String NumAlterLogDirsReplicationQuotaSamplesProp = "alter.log.dirs.replication.quota.window.num";
          public static String NumControllerQuotaSamplesProp = "controller.quota.window.num";
          public static String QuotaWindowSizeSecondsProp = "quota.window.size.seconds";
          public static String ReplicationQuotaWindowSizeSecondsProp = "replication.quota.window.size.seconds";
          public static String AlterLogDirsReplicationQuotaWindowSizeSecondsProp = "alter.log.dirs.replication.quota.window.size.seconds";
          public static String ControllerQuotaWindowSizeSecondsProp = "controller.quota.window.size.seconds";
          public static String ClientQuotaCallbackClassProp = "client.quota.callback.class";

          public static String DeleteTopicEnableProp = "delete.topic.enable";
          public static String CompressionTypeProp = ServerTopicConfigSynonyms.serverSynonym(TopicConfig.COMPRESSION_TYPE_CONFIG);

          /** ********* Kafka Metrics Configuration ***********/
          public static String MetricSampleWindowMsProp = CommonClientConfigs.METRICS_SAMPLE_WINDOW_MS_CONFIG;
          public static String MetricNumSamplesProp = CommonClientConfigs.METRICS_NUM_SAMPLES_CONFIG;
          public static String MetricReporterClassesProp = CommonClientConfigs.METRIC_REPORTER_CLASSES_CONFIG;
          public static String MetricRecordingLevelProp = CommonClientConfigs.METRICS_RECORDING_LEVEL_CONFIG;
    @Deprecated
  String AutoIncludeJmxReporterProp = CommonClientConfigs.AUTO_INCLUDE_JMX_REPORTER_CONFIG;

          /** ********* Kafka Yammer Metrics Reporters Configuration ***********/
          public static String KafkaMetricsReporterClassesProp = "kafka.metrics.reporters";
          public static String KafkaMetricsPollingIntervalSecondsProp = "kafka.metrics.polling.interval.secs";

          /** ********* Kafka Client Telemetry Metrics Configuration ***********/
          public static String ClientTelemetryMaxBytesProp = "telemetry.max.bytes";

          /** ******** Common Security Configuration *************/
          public static String PrincipalBuilderClassProp = BrokerSecurityConfigs.PRINCIPAL_BUILDER_CLASS_CONFIG;
          public static String ConnectionsMaxReauthMsProp = BrokerSecurityConfigs.CONNECTIONS_MAX_REAUTH_MS;
          public static String SaslServerMaxReceiveSizeProp = BrokerSecurityConfigs.SASL_SERVER_MAX_RECEIVE_SIZE_CONFIG;
          public static String securityProviderClassProp = SecurityConfig.SECURITY_PROVIDERS_CONFIG;

          /** ********* SSL Configuration ****************/
          public static String SslProtocolProp = SslConfigs.SSL_PROTOCOL_CONFIG;
          public static String SslProviderProp = SslConfigs.SSL_PROVIDER_CONFIG;
          public static String SslCipherSuitesProp = SslConfigs.SSL_CIPHER_SUITES_CONFIG;
          public static String SslEnabledProtocolsProp = SslConfigs.SSL_ENABLED_PROTOCOLS_CONFIG;
          public static String SslKeystoreTypeProp = SslConfigs.SSL_KEYSTORE_TYPE_CONFIG;
          public static String SslKeystoreLocationProp = SslConfigs.SSL_KEYSTORE_LOCATION_CONFIG;
          public static String SslKeystorePasswordProp = SslConfigs.SSL_KEYSTORE_PASSWORD_CONFIG;
          public static String SslKeyPasswordProp = SslConfigs.SSL_KEY_PASSWORD_CONFIG;
          public static String SslKeystoreKeyProp = SslConfigs.SSL_KEYSTORE_KEY_CONFIG;
          public static String SslKeystoreCertificateChainProp = SslConfigs.SSL_KEYSTORE_CERTIFICATE_CHAIN_CONFIG;
          public static String SslTruststoreTypeProp = SslConfigs.SSL_TRUSTSTORE_TYPE_CONFIG;
          public static String SslTruststoreLocationProp = SslConfigs.SSL_TRUSTSTORE_LOCATION_CONFIG;
          public static String SslTruststorePasswordProp = SslConfigs.SSL_TRUSTSTORE_PASSWORD_CONFIG;
          public static String SslTruststoreCertificatesProp = SslConfigs.SSL_TRUSTSTORE_CERTIFICATES_CONFIG;
          public static String SslKeyManagerAlgorithmProp = SslConfigs.SSL_KEYMANAGER_ALGORITHM_CONFIG;
          public static String SslTrustManagerAlgorithmProp = SslConfigs.SSL_TRUSTMANAGER_ALGORITHM_CONFIG;
          public static String SslEndpointIdentificationAlgorithmProp = SslConfigs.SSL_ENDPOINT_IDENTIFICATION_ALGORITHM_CONFIG;
          public static String SslSecureRandomImplementationProp = SslConfigs.SSL_SECURE_RANDOM_IMPLEMENTATION_CONFIG;
          public static String SslClientAuthProp = BrokerSecurityConfigs.SSL_CLIENT_AUTH_CONFIG;
          public static String SslPrincipalMappingRulesProp = BrokerSecurityConfigs.SSL_PRINCIPAL_MAPPING_RULES_CONFIG;
          public static String SslEngineFactoryClassProp = SslConfigs.SSL_ENGINE_FACTORY_CLASS_CONFIG;
          public static String SslAllowDnChangesProp = BrokerSecurityConfigs.SSL_ALLOW_DN_CHANGES_CONFIG;
          public static String SslAllowSanChangesProp = BrokerSecurityConfigs.SSL_ALLOW_SAN_CHANGES_CONFIG;

          /** ********* SASL Configuration ****************/
          public static String SaslMechanismInterBrokerProtocolProp = "sasl.mechanism.inter.broker.protocol";
          public static String SaslJaasConfigProp = SaslConfigs.SASL_JAAS_CONFIG;
          public static String SaslEnabledMechanismsProp = BrokerSecurityConfigs.SASL_ENABLED_MECHANISMS_CONFIG;
          public static String SaslServerCallbackHandlerClassProp = BrokerSecurityConfigs.SASL_SERVER_CALLBACK_HANDLER_CLASS;
          public static String SaslClientCallbackHandlerClassProp = SaslConfigs.SASL_CLIENT_CALLBACK_HANDLER_CLASS;
          public static String SaslLoginClassProp = SaslConfigs.SASL_LOGIN_CLASS;
          public static String SaslLoginCallbackHandlerClassProp = SaslConfigs.SASL_LOGIN_CALLBACK_HANDLER_CLASS;
          public static String SaslKerberosServiceNameProp = SaslConfigs.SASL_KERBEROS_SERVICE_NAME;
          public static String SaslKerberosKinitCmdProp = SaslConfigs.SASL_KERBEROS_KINIT_CMD;
          public static String SaslKerberosTicketRenewWindowFactorProp = SaslConfigs.SASL_KERBEROS_TICKET_RENEW_WINDOW_FACTOR;
          public static String SaslKerberosTicketRenewJitterProp = SaslConfigs.SASL_KERBEROS_TICKET_RENEW_JITTER;
          public static String SaslKerberosMinTimeBeforeReloginProp = SaslConfigs.SASL_KERBEROS_MIN_TIME_BEFORE_RELOGIN;
          public static String SaslKerberosPrincipalToLocalRulesProp = BrokerSecurityConfigs.SASL_KERBEROS_PRINCIPAL_TO_LOCAL_RULES_CONFIG;
          public static String SaslLoginRefreshWindowFactorProp = SaslConfigs.SASL_LOGIN_REFRESH_WINDOW_FACTOR;
          public static String SaslLoginRefreshWindowJitterProp = SaslConfigs.SASL_LOGIN_REFRESH_WINDOW_JITTER;
          public static String SaslLoginRefreshMinPeriodSecondsProp = SaslConfigs.SASL_LOGIN_REFRESH_MIN_PERIOD_SECONDS;
          public static String SaslLoginRefreshBufferSecondsProp = SaslConfigs.SASL_LOGIN_REFRESH_BUFFER_SECONDS;

          public static String SaslLoginConnectTimeoutMsProp = SaslConfigs.SASL_LOGIN_CONNECT_TIMEOUT_MS;
          public static String SaslLoginReadTimeoutMsProp = SaslConfigs.SASL_LOGIN_READ_TIMEOUT_MS;
          public static String SaslLoginRetryBackoffMaxMsProp = SaslConfigs.SASL_LOGIN_RETRY_BACKOFF_MAX_MS;
          public static String SaslLoginRetryBackoffMsProp = SaslConfigs.SASL_LOGIN_RETRY_BACKOFF_MS;
          public static String SaslOAuthBearerScopeClaimNameProp = SaslConfigs.SASL_OAUTHBEARER_SCOPE_CLAIM_NAME;
          public static String SaslOAuthBearerSubClaimNameProp = SaslConfigs.SASL_OAUTHBEARER_SUB_CLAIM_NAME;
          public static String SaslOAuthBearerTokenEndpointUrlProp = SaslConfigs.SASL_OAUTHBEARER_TOKEN_ENDPOINT_URL;
          public static String SaslOAuthBearerJwksEndpointUrlProp = SaslConfigs.SASL_OAUTHBEARER_JWKS_ENDPOINT_URL;
          public static String SaslOAuthBearerJwksEndpointRefreshMsProp = SaslConfigs.SASL_OAUTHBEARER_JWKS_ENDPOINT_REFRESH_MS;
          public static String SaslOAuthBearerJwksEndpointRetryBackoffMaxMsProp = SaslConfigs.SASL_OAUTHBEARER_JWKS_ENDPOINT_RETRY_BACKOFF_MAX_MS;
          public static String SaslOAuthBearerJwksEndpointRetryBackoffMsProp = SaslConfigs.SASL_OAUTHBEARER_JWKS_ENDPOINT_RETRY_BACKOFF_MS;
          public static String SaslOAuthBearerClockSkewSecondsProp = SaslConfigs.SASL_OAUTHBEARER_CLOCK_SKEW_SECONDS;
          public static String SaslOAuthBearerExpectedAudienceProp = SaslConfigs.SASL_OAUTHBEARER_EXPECTED_AUDIENCE;
          public static String SaslOAuthBearerExpectedIssuerProp = SaslConfigs.SASL_OAUTHBEARER_EXPECTED_ISSUER;

          /** ********* Delegation Token Configuration ****************/
          public static String DelegationTokenSecretKeyAliasProp = "delegation.token.master.key";
          public static String DelegationTokenSecretKeyProp = "delegation.token.secret.key";
          public static String DelegationTokenMaxLifeTimeProp = "delegation.token.max.lifetime.ms";
          public static String DelegationTokenExpiryTimeMsProp = "delegation.token.expiry.time.ms";
          public static String DelegationTokenExpiryCheckIntervalMsProp = "delegation.token.expiry.check.interval.ms";

          /** ********* Password encryption configuration for dynamic configs *********/
          public static String PasswordEncoderSecretProp = PasswordEncoderConfigs.SECRET;
          public static String PasswordEncoderOldSecretProp = PasswordEncoderConfigs.OLD_SECRET;
          public static String PasswordEncoderKeyFactoryAlgorithmProp = PasswordEncoderConfigs.KEYFACTORY_ALGORITHM;
          public static String PasswordEncoderCipherAlgorithmProp = PasswordEncoderConfigs.CIPHER_ALGORITHM;
          public static String PasswordEncoderKeyLengthProp = PasswordEncoderConfigs.KEY_LENGTH;
          public static String PasswordEncoderIterationsProp = PasswordEncoderConfigs.ITERATIONS;

          /** Internal Configurations **/
          public static String UnstableApiVersionsEnableProp = "unstable.api.versions.enable";
          public static String UnstableMetadataVersionsEnableProp = "unstable.metadata.versions.enable";

          /* Documentation */
          /** ********* Zookeeper Configuration ***********/
          public static String ZkConnectDoc = "Specifies the ZooKeeper connection string in the form <code>hostname:port</code> where host and port are the " +
          "host and port of a ZooKeeper server. To allow connecting through other ZooKeeper nodes when that ZooKeeper machine is " +
          "down you can also specify multiple hosts in the form <code>hostname1:port1,hostname2:port2,hostname3:port3</code>.\n" +
          "The server can also have a ZooKeeper chroot path as part of its ZooKeeper connection string which puts its data under some path in the global ZooKeeper namespace. " +
          "For example to give a chroot path of <code>/chroot/path</code> you would give the connection string as <code>hostname1:port1,hostname2:port2,hostname3:port3/chroot/path</code>.";
          public static String ZkSessionTimeoutMsDoc = "Zookeeper session timeout";
          public static String ZkConnectionTimeoutMsDoc = "The max time that the client waits to establish a connection to ZooKeeper. If not set, the value in " + ZkSessionTimeoutMsProp + " is used";
          public static String ZkEnableSecureAclsDoc = "Set client to use secure ACLs";
          public static String ZkMaxInFlightRequestsDoc = "The maximum number of unacknowledged requests the client will send to ZooKeeper before blocking.";
          public static String ZkSslClientEnableDoc = "Set client to use TLS when connecting to ZooKeeper." +
          " An explicit value overrides any value set via the <code>zookeeper.client.secure</code> system property (note the different name)." +
          String.format(" Defaults to false if neither is set; when true, <code>%s</code> must be set (typically to <code>org.apache.zookeeper.ClientCnxnSocketNetty</code>); other values to set may include ", ZkClientCnxnSocketProp) +
          ZkSslConfigToSystemPropertyMap.keySet().stream().filter(x -> x != ZkSslClientEnableProp && x != ZkClientCnxnSocketProp).sorted().collect(Collectors.joining("<code>", "</code>, <code>", "</code>"));
          public static String ZkClientCnxnSocketDoc = "Typically set to <code>org.apache.zookeeper.ClientCnxnSocketNetty</code> when using TLS connectivity to ZooKeeper." +
          String.format(" Overrides any explicit value set via the same-named <code>%s</code> system property.", ZkSslConfigToSystemPropertyMap.get(ZkClientCnxnSocketProp));
          public static String ZkSslKeyStoreLocationDoc = "Keystore location when using a client-side certificate with TLS connectivity to ZooKeeper." +
          String.format(" Overrides any explicit value set via the <code>%s</code> system property (note the camelCase).", ZkSslConfigToSystemPropertyMap.get(ZkSslKeyStoreLocationProp));
          public static String ZkSslKeyStorePasswordDoc = "Keystore password when using a client-side certificate with TLS connectivity to ZooKeeper." +
          String.format(" Overrides any explicit value set via the <code>%s</code> system property (note the camelCase).", ZkSslConfigToSystemPropertyMap.get(ZkSslKeyStorePasswordProp)) +
          " Note that ZooKeeper does not support a key password different from the keystore password, so be sure to set the key password in the keystore to be identical to the keystore password; otherwise the connection attempt to Zookeeper will fail.";
          public static String ZkSslKeyStoreTypeDoc = "Keystore type when using a client-side certificate with TLS connectivity to ZooKeeper." +
                  String.format(" Overrides any explicit value set via the <code>٪س</code> system property (note the camelCase).", ZkSslConfigToSystemPropertyMap.get(ZkSslKeyStoreTypeProp)) +
          " The default value of <code>null</code> means the type will be auto-detected based on the filename extension of the keystore.";
          public static String ZkSslTrustStoreLocationDoc = "Truststore location when using TLS connectivity to ZooKeeper." +
          String.format(" Overrides any explicit value set via the <code>%s</code> system property (note the camelCase).", ZkSslConfigToSystemPropertyMap.get(ZkSslTrustStoreLocationProp));
          public static String ZkSslTrustStorePasswordDoc = "Truststore password when using TLS connectivity to ZooKeeper." +
          String.format(" Overrides any explicit value set via the <code>%s</code> system property (note the camelCase).", ZkSslConfigToSystemPropertyMap.get(ZkSslTrustStorePasswordProp));
          public static String ZkSslTrustStoreTypeDoc = "Truststore type when using TLS connectivity to ZooKeeper." +
          String.format(" Overrides any explicit value set via the <code>%s</code> system property (note the camelCase).", ZkSslConfigToSystemPropertyMap.get(ZkSslTrustStoreTypeProp)+
          " The default value of <code>null</code> means the type will be auto-detected based on the filename extension of the truststore.";
          public static String ZkSslProtocolDoc = "Specifies the protocol to be used in ZooKeeper TLS negotiation." +
          String.format(" An explicit value overrides any value set via the same-named <code>%s</code> system property.", ZkSslConfigToSystemPropertyMap.get(ZkSslProtocolProp));
          public static String ZkSslEnabledProtocolsDoc = "Specifies the enabled protocol(s) in ZooKeeper TLS negotiation (csv)." +
          String.format(" Overrides any explicit value set via the <code>%s</code> system property (note the camelCase).", ZkSslConfigToSystemPropertyMap.get(ZkSslEnabledProtocolsProp)) +
          String.format(" The default value of <code>null</code> means the enabled protocol will be the value of the <code>%s</code> configuration property.", KafkaConfig.ZkSslProtocolProp);
          public static String ZkSslCipherSuitesDoc = "Specifies the enabled cipher suites to be used in ZooKeeper TLS negotiation (csv)." +
          String.format(" Overrides any explicit value set via the <code>%s</code> system property (note the single word \"ciphersuites\").", ZkSslConfigToSystemPropertyMap.get(ZkSslCipherSuitesProp)) +
          " The default value of <code>null</code> means the list of enabled cipher suites is determined by the Java runtime being used.";
          public static String ZkSslEndpointIdentificationAlgorithmDoc = "Specifies whether to enable hostname verification in the ZooKeeper TLS negotiation process, with (case-insensitively) \"https\" meaning ZooKeeper hostname verification is enabled and an explicit blank value meaning it is disabled (disabling it is only recommended for testing purposes)." +
          String.format(" An explicit value overrides any \"true\" or \"false\" value set via the <code>%s</code> system property (note the different name and values; true implies https and false implies blank).", ZkSslConfigToSystemPropertyMap.get(ZkSslEndpointIdentificationAlgorithmProp));
          public static String ZkSslCrlEnableDoc = "Specifies whether to enable Certificate Revocation List in the ZooKeeper TLS protocols." +
          String.format(" Overrides any explicit value set via the <code>%s</code> system property (note the shorter name).", ZkSslConfigToSystemPropertyMap.get(ZkSslCrlEnableProp));
          public static String ZkSslOcspEnableDoc = "Specifies whether to enable Online Certificate Status Protocol in the ZooKeeper TLS protocols." +
          String.format(" Overrides any explicit value set via the <code>%s</code> system property (note the shorter name).", ZkSslConfigToSystemPropertyMap.get(ZkSslOcspEnableProp));
          /** ********* General Configuration ***********/
          public static String BrokerIdGenerationEnableDoc = String.format("Enable automatic broker id generation on the server. When enabled the value configured for %s should be reviewed.", MaxReservedBrokerIdProp);
          public static String MaxReservedBrokerIdDoc = "Max number that can be used for a broker.id";
          public static String BrokerIdDoc = "The broker id for this server. If unset, a unique broker id will be generated." +
          "To avoid conflicts between ZooKeeper generated broker id's and user configured broker id's, generated broker ids " +
          "start from " + MaxReservedBrokerIdProp + " + 1.";
          public static String MessageMaxBytesDoc = TopicConfig.MAX_MESSAGE_BYTES_DOC +
          String.format("This can be set per topic with the topic level <code>%s</code> config.", TopicConfig.MAX_MESSAGE_BYTES_CONFIG);
          public static String NumNetworkThreadsDoc = "The number of threads that the server uses for receiving requests from the network and sending responses to the network. Noted: each listener (except for controller listener) creates its own thread pool.";
          public static String NumIoThreadsDoc = "The number of threads that the server uses for processing requests, which may include disk I/O";
          public static String NumReplicaAlterLogDirsThreadsDoc = "The number of threads that can move replicas between log directories, which may include disk I/O";
          public static String BackgroundThreadsDoc = "The number of threads to use for various background processing tasks";
          public static String QueuedMaxRequestsDoc = "The number of queued requests allowed for data-plane, before blocking the network threads";
          public static String QueuedMaxRequestBytesDoc = "The number of queued bytes allowed before no more requests are read";
          public static String RequestTimeoutMsDoc = CommonClientConfigs.REQUEST_TIMEOUT_MS_DOC;
          public static String ConnectionSetupTimeoutMsDoc = CommonClientConfigs.SOCKET_CONNECTION_SETUP_TIMEOUT_MS_DOC;
          public static String ConnectionSetupTimeoutMaxMsDoc = CommonClientConfigs.SOCKET_CONNECTION_SETUP_TIMEOUT_MAX_MS_DOC;

          /** KRaft mode configs */
          public static String ProcessRolesDoc = "The roles that this process plays: 'broker', 'controller', or 'broker,controller' if it is both. " +
          "This configuration is only applicable for clusters in KRaft (Kafka Raft) mode (instead of ZooKeeper). Leave this config undefined or empty for ZooKeeper clusters.";
          public static String InitialBrokerRegistrationTimeoutMsDoc = "When initially registering with the controller quorum, the number of milliseconds to wait before declaring failure and exiting the broker process.";
          public static String BrokerHeartbeatIntervalMsDoc = "The length of time in milliseconds between broker heartbeats. Used when running in KRaft mode.";
          public static String BrokerSessionTimeoutMsDoc = "The length of time in milliseconds that a broker lease lasts if no heartbeats are made. Used when running in KRaft mode.";
          public static String NodeIdDoc = "The node ID associated with the roles this process is playing when <code>process.roles</code> is non-empty. " +
          "This is required configuration when running in KRaft mode."
          public static String MetadataLogDirDoc = "This configuration determines where we put the metadata log for clusters in KRaft mode. " +
          "If it is not set, the metadata log is placed in the first log directory from log.dirs.";
          public static String MetadataSnapshotMaxNewRecordBytesDoc = "This is the maximum number of bytes in the log between the latest " +
          "snapshot and the high-watermark needed before generating a new snapshot. The default value is " +
          String.format("%s. To generate snapshots based on the time elapsed, see the <code>%s</code> configuration. The Kafka node will generate a snapshot when " ,
                  Defaults.METADATA_SNAPSHOT_MAX_NEW_RECORD_BYTES, MetadataSnapshotMaxIntervalMsProp) +
          "either the maximum time interString is reached or the maximum bytes limit is reached.";
          public static String MetadataSnapshotMaxIntervalMsDoc = "This is the maximum number of milliseconds to wait to generate a snapshot " +
          "if there are committed records in the log that are not included in the latest snapshot. A value of zero disables " +
          s"time based snapshot generation. The default value is ${Defaults.METADATA_SNAPSHOT_MAX_INTERVAL_MS}. To generate " +
          s"snapshots based on the number of metadata bytes, see the <code>$MetadataSnapshotMaxNewRecordBytesProp</code> " +
          "configuration. The Kafka node will generate a snapshot when either the maximum time interString is reached or the " +
          "maximum bytes limit is reached.";
          public static String MetadataMaxIdleIntervalMsDoc = "This configuration controls how often the active " +
          "controller should write no-op records to the metadata partition. If the value is 0, no-op records " +
          s"are not appended to the metadata partition. The default value is ${Defaults.METADATA_MAX_IDLE_INTERVAL_MS}";
          public static String ControllerListenerNamesDoc = "A comma-separated list of the names of the listeners used by the controller. This is required " +
          "if running in KRaft mode. When communicating with the controller quorum, the broker will always use the first listener in this list.\n " +
          "Note: The ZooKeeper-based controller should not set this configuration."
          public static String SaslMechanismControllerProtocolDoc = "SASL mechanism used for communication with controllers. Default is GSSAPI.";
          public static String MetadataLogSegmentBytesDoc = "The maximum size of a single metadata log file.";
          public static String MetadataLogSegmentMinBytesDoc = "Override the minimum size for a single metadata log file. This should be used for testing only.";
          public static String ServerMaxStartupTimeMsDoc = "The maximum number of milliseconds we will wait for the server to come up. " +
          "By default there is no limit. This should be used for testing only.";

          public static String MetadataLogSegmentMillisDoc = "The maximum time before a new metadata log file is rolled out (in milliseconds).";
          public static String MetadataMaxRetentionBytesDoc = "The maximum combined size of the metadata log and snapshots before deleting old " +
          "snapshots and log files. Since at least one snapshot must exist before any logs can be deleted, this is a soft limit.";
          public static String MetadataMaxRetentionMillisDoc = "The number of milliseconds to keep a metadata log file or snapshot before " +
          "deleting it. Since at least one snapshot must exist before any logs can be deleted, this is a soft limit.";

          /************* Authorizer Configuration ***********/
          public static String AuthorizerClassNameDoc = s"The fully qualified name of a class that implements <code>${classOf[Authorizer].getName}</code>" +
          " interface, which is used by the broker for authorization.";
          public static String EarlyStartListenersDoc = "A comma-separated list of listener names which may be started before the authorizer has finished " +
          "initialization. This is useful when the authorizer is dependent on the cluster itself for bootstrapping, as is the case for " +
          "the StandardAuthorizer (which stores ACLs in the metadata log.) By default, all listeners included in controller.listener.names " +
          "will also be early start listeners. A listener should not appear in this list if it accepts external traffic.";

          /** ********* Socket Server Configuration ***********/
          public static String ListenersDoc = "Listener List - Comma-separated list of URIs we will listen on and the listener names." +
          s" If the listener name is not a security protocol, <code>$ListenerSecurityProtocolMapProp</code> must also be set.\n" +
          " Listener names and port numbers must be unique unless \n" +
          " one listener is an IPv4 address and the other listener is \n" +
          " an IPv6 address (for the same port).\n" +
          " Specify hostname as 0.0.0.0 to bind to all interfaces.\n" +
          " Leave hostname empty to bind to default interface.\n" +
          " Examples of legal listener lists:\n" +
          " <code>PLAINTEXT://myhost:9092,SSL://:9091</code>\n" +
          " <code>CLIENT://0.0.0.0:9092,REPLICATION://localhost:9093</code>\n" +
          " <code>PLAINTEXT://127.0.0.1:9092,SSL://[::1]:9092</code>\n";
          public static String AdvertisedListenersDoc = s"Listeners to publish to ZooKeeper for clients to use, if different than the <code>$ListenersProp</code> config property." +
          " In IaaS environments, this may need to be different from the interface to which the broker binds." +
          s" If this is not set, the value for <code>$ListenersProp</code> will be used." +
          s" Unlike <code>$ListenersProp</code>, it is not valid to advertise the 0.0.0.0 meta-address.\n" +
          s" Also unlike <code>$ListenersProp</code>, there can be duplicated ports in this property," +
          " so that one listener can be configured to advertise another listener's address." +
          " This can be useful in some cases where external load balancers are used.";
          public static String ListenerSecurityProtocolMapDoc = "Map between listener names and security protocols. This must be defined for " +
          "the same security protocol to be usable in more than one port or IP. For example, internal and " +
          "external traffic can be separated even if SSL is required for both. Concretely, the user could define listeners " +
          "with names INTERNAL and EXTERNAL and this property as: <code>INTERNAL:SSL,EXTERNAL:SSL</code>. As shown, key and value are " +
          "separated by a colon and map entries are separated by commas. Each listener name should only appear once in the map. " +
          "Different security (SSL and SASL) settings can be configured for each listener by adding a normalised " +
          "prefix (the listener name is lowercased) to the config name. For example, to set a different keystore for the " +
          "INTERNAL listener, a config with name <code>listener.name.internal.ssl.keystore.location</code> would be set. " +
          "If the config for the listener name is not set, the config will fallback to the generic config (i.e. <code>ssl.keystore.location</code>). " +
          "Note that in KRaft a default mapping from the listener names defined by <code>controller.listener.names</code> to PLAINTEXT " +
          "is assumed if no explicit mapping is provided and no other security protocol is in use.";
          public static String controlPlaneListenerNameDoc = "Name of listener used for communication between controller and brokers. " +
          s"A broker will use the <code>$ControlPlaneListenerNameProp</code> to locate the endpoint in $ListenersProp list, to listen for connections from the controller. " +
          "For example, if a broker's config is:\n" +
          "<code>listeners = INTERNAL://192.1.1.8:9092, EXTERNAL://10.1.1.5:9093, CONTROLLER://192.1.1.8:9094" +
          "listener.security.protocol.map = INTERNAL:PLAINTEXT, EXTERNAL:SSL, CONTROLLER:SSL" +
          "control.plane.listener.name = CONTROLLER</code>\n" +
          "On startup, the broker will start listening on \"192.1.1.8:9094\" with security protocol \"SSL\".\n" +
          s"On the controller side, when it discovers a broker's published endpoints through ZooKeeper, it will use the <code>$ControlPlaneListenerNameProp</code> " +
          "to find the endpoint, which it will use to establish connection to the broker.\n" +
          "For example, if the broker's published endpoints on ZooKeeper are:\n" +
          " <code>\"endpoints\" : [\"INTERNAL://broker1.example.com:9092\",\"EXTERNAL://broker1.example.com:9093\",\"CONTROLLER://broker1.example.com:9094\"]</code>\n" +
          " and the controller's config is:\n" +
          "<code>listener.security.protocol.map = INTERNAL:PLAINTEXT, EXTERNAL:SSL, CONTROLLER:SSL" +
          "control.plane.listener.name = CONTROLLER</code>\n" +
          "then the controller will use \"broker1.example.com:9094\" with security protocol \"SSL\" to connect to the broker.\n" +
          "If not explicitly configured, the default value will be null and there will be no dedicated endpoints for controller connections.\n" +
          s"If explicitly configured, the value cannot be the same as the value of <code>$InterBrokerListenerNameProp</code>.";

          public static String SocketSendBufferBytesDoc = "The SO_SNDBUF buffer of the socket server sockets. If the value is -1, the OS default will be used.";
          public static String SocketReceiveBufferBytesDoc = "The SO_RCVBUF buffer of the socket server sockets. If the value is -1, the OS default will be used.";
          public static String SocketRequestMaxBytesDoc = "The maximum number of bytes in a socket request";
          public static String SocketListenBacklogSizeDoc = "The maximum number of pending connections on the socket. " +
          "In Linux, you may also need to configure <code>somaxconn</code> and <code>tcp_max_syn_backlog</code> kernel parameters " +
          "accordingly to make the configuration takes effect.";
          public static String MaxConnectionsPerIpDoc = "The maximum number of connections we allow from each ip address. This can be set to 0 if there are overrides " +
          s"configured using $MaxConnectionsPerIpOverridesProp property. New connections from the ip address are dropped if the limit is reached.";
          public static String MaxConnectionsPerIpOverridesDoc = "A comma-separated list of per-ip or hostname overrides to the default maximum number of connections. " +
          "An example value is \"hostName:100,127.0.0.1:200\""
          public static String MaxConnectionsDoc = "The maximum number of connections we allow in the broker at any time. This limit is applied in addition " +
          s"to any per-ip limits configured using $MaxConnectionsPerIpProp. Listener-level limits may also be configured by prefixing the " +
          s"config name with the listener prefix, for example, <code>listener.name.internal.$MaxConnectionsProp</code>. Broker-wide limit " +
          "should be configured based on broker capacity while listener limits should be configured based on application requirements. " +
          "New connections are blocked if either the listener or broker limit is reached. Connections on the inter-broker listener are " +
          "permitted even if broker-wide limit is reached. The least recently used connection on another listener will be closed in this case.";
          public static String MaxConnectionCreationRateDoc = "The maximum connection creation rate we allow in the broker at any time. Listener-level limits " +
          s"may also be configured by prefixing the config name with the listener prefix, for example, <code>listener.name.internal.$MaxConnectionCreationRateProp</code>." +
          "Broker-wide connection rate limit should be configured based on broker capacity while listener limits should be configured based on " +
          "application requirements. New connections will be throttled if either the listener or the broker limit is reached, with the exception " +
          "of inter-broker listener. Connections on the inter-broker listener will be throttled only when the listener-level rate limit is reached.";
          public static String ConnectionsMaxIdleMsDoc = "Idle connections timeout: the server socket processor threads close the connections that idle more than this";
          public static String FailedAuthenticationDelayMsDoc = "Connection close delay on failed authentication: this is the time (in milliseconds) by which connection close will be delayed on authentication failure. " +
          s"This must be configured to be less than $ConnectionsMaxIdleMsProp to prevent connection timeout.";
          /************* Rack Configuration **************/
          public static String RackDoc = "Rack of the broker. This will be used in rack aware replication assignment for fault tolerance. Examples: <code>RACK1</code>, <code>us-east-1d</code>";
          /** ********* Log Configuration ***********/
          public static String NumPartitionsDoc = "The default number of log partitions per topic";
          public static String LogDirDoc = "The directory in which the log data is kept (supplemental for " + LogDirsProp + " property)";
          public static String LogDirsDoc = "A comma-separated list of the directories where the log data is stored. If not set, the value in " + LogDirProp + " is used.";
          public static String LogSegmentBytesDoc = "The maximum size of a single log file";
          public static String LogRollTimeMillisDoc = "The maximum time before a new log segment is rolled out (in milliseconds). If not set, the value in " + LogRollTimeHoursProp + " is used";
          public static String LogRollTimeHoursDoc = "The maximum time before a new log segment is rolled out (in hours), secondary to " + LogRollTimeMillisProp + " property";

          public static String LogRollTimeJitterMillisDoc = "The maximum jitter to subtract from logRollTimeMillis (in milliseconds). If not set, the value in " + LogRollTimeJitterHoursProp + " is used";
          public static String LogRollTimeJitterHoursDoc = "The maximum jitter to subtract from logRollTimeMillis (in hours), secondary to " + LogRollTimeJitterMillisProp + " property";

          public static String LogRetentionTimeMillisDoc = "The number of milliseconds to keep a log file before deleting it (in milliseconds), If not set, the value in " + LogRetentionTimeMinutesProp + " is used. If set to -1, no time limit is applied.";
          public static String LogRetentionTimeMinsDoc = "The number of minutes to keep a log file before deleting it (in minutes), secondary to " + LogRetentionTimeMillisProp + " property. If not set, the value in " + LogRetentionTimeHoursProp + " is used";
          public static String LogRetentionTimeHoursDoc = "The number of hours to keep a log file before deleting it (in hours), tertiary to " + LogRetentionTimeMillisProp + " property";

          public static String LogRetentionBytesDoc = "The maximum size of the log before deleting it";
          public static String LogCleanupIntervalMsDoc = "The frequency in milliseconds that the log cleaner checks whether any log is eligible for deletion";
          public static String LogCleanupPolicyDoc = "The default cleanup policy for segments beyond the retention window. A comma separated list of valid policies. Valid policies are: \"delete\" and \"compact\"";
          public static String LogCleanerThreadsDoc = "The number of background threads to use for log cleaning";
          public static String LogCleanerIoMaxBytesPerSecondDoc = "The log cleaner will be throttled so that the sum of its read and write i/o will be less than this value on average";
          public static String LogCleanerDedupeBufferSizeDoc = "The total memory used for log deduplication across all cleaner threads";
          public static String LogCleanerIoBufferSizeDoc = "The total memory used for log cleaner I/O buffers across all cleaner threads";
          public static String LogCleanerDedupeBufferLoadFactorDoc = "Log cleaner dedupe buffer load factor. The percentage full the dedupe buffer can become. A higher value " +
          "will allow more log to be cleaned at once but will lead to more hash collisions"
          public static String LogCleanerBackoffMsDoc = "The amount of time to sleep when there are no logs to clean";
          public static String LogCleanerMinCleanRatioDoc = "The minimum ratio of dirty log to total log for a log to eligible for cleaning. " +
          "If the " + LogCleanerMaxCompactionLagMsProp + " or the " + LogCleanerMinCompactionLagMsProp +
          " configurations are also specified, then the log compactor considers the log eligible for compaction " +
          "as soon as either: (i) the dirty ratio threshold has been met and the log has had dirty (uncompacted) " +
          "records for at least the " + LogCleanerMinCompactionLagMsProp + " duration, or (ii) if the log has had " +
          "dirty (uncompacted) records for at most the " + LogCleanerMaxCompactionLagMsProp + " period."
          public static String LogCleanerEnableDoc = "Enable the log cleaner process to run on the server. Should be enabled if using any topics with a cleanup.policy=compact including the internal offsets topic. If disabled those topics will not be compacted and continually grow in size.";
          public static String LogCleanerDeleteRetentionMsDoc = "The amount of time to retain tombstone message markers for log compacted topics. This setting also gives a bound " +
          "on the time in which a consumer must complete a read if they begin from offset 0 to ensure that they get a valid snapshot of the final stage (otherwise  " +
          "tombstones messages may be collected before a consumer completes their scan)."
          public static String LogCleanerMinCompactionLagMsDoc = "The minimum time a message will remain uncompacted in the log. Only applicable for logs that are being compacted.";
          public static String LogCleanerMaxCompactionLagMsDoc = "The maximum time a message will remain ineligible for compaction in the log. Only applicable for logs that are being compacted.";
          public static String LogIndexSizeMaxBytesDoc = "The maximum size in bytes of the offset index";
          public static String LogIndexIntervalBytesDoc = "The interString with which we add an entry to the offset index.";
          public static String LogFlushIntervalMessagesDoc = "The number of messages accumulated on a log partition before messages are flushed to disk.";
          public static String LogDeleteDelayMsDoc = "The amount of time to wait before deleting a file from the filesystem";
          public static String LogFlushSchedulerIntervalMsDoc = "The frequency in ms that the log flusher checks whether any log needs to be flushed to disk";
          public static String LogFlushIntervalMsDoc = "The maximum time in ms that a message in any topic is kept in memory before flushed to disk. If not set, the value in " + LogFlushSchedulerIntervalMsProp + " is used";
          public static String LogFlushOffsetCheckpointIntervalMsDoc = "The frequency with which we update the persistent record of the last flush which acts as the log recovery point.";
          public static String LogFlushStartOffsetCheckpointIntervalMsDoc = "The frequency with which we update the persistent record of log start offset";
          public static String LogPreAllocateEnableDoc = "Should pre allocate file when create new segment? If you are using Kafka on Windows, you probably need to set it to true.";
          public static String LogMessageFormatVersionDoc = "Specify the message format version the broker will use to append messages to the logs. The value should be a valid MetadataVersion. " +
          "Some examples are: 0.8.2, 0.9.0.0, 0.10.0, check MetadataVersion for more details. By setting a particular message format version, the " +
          "user is certifying that all the existing messages on disk are smaller or equal than the specified version. Setting this value incorrectly " +
          "will cause consumers with older versions to break as they will receive messages with a format that they don't understand."

          public static String LogMessageTimestampTypeDoc = "Define whether the timestamp in the message is message create time or log append time. The value should be either " +
          "<code>CreateTime</code> or <code>LogAppendTime</code>."

          public static String LogMessageTimestampDifferenceMaxMsDoc = "[DEPRECATED] The maximum difference allowed between the timestamp when a broker receives " +
          "a message and the timestamp specified in the message. If log.message.timestamp.type=CreateTime, a message will be rejected " +
          "if the difference in timestamp exceeds this threshold. This configuration is ignored if log.message.timestamp.type=LogAppendTime." +
          "The maximum timestamp difference allowed should be no greater than log.retention.ms to avoid unnecessarily frequent log rolling."

          public static String LogMessageTimestampBeforeMaxMsDoc = "This configuration sets the allowable timestamp difference between the " +
          "broker's timestamp and the message timestamp. The message timestamp can be earlier than or equal to the broker's " +
          "timestamp, with the maximum allowable difference determined by the value set in this configuration. " +
          "If log.message.timestamp.type=CreateTime, the message will be rejected if the difference in timestamps exceeds " +
          "this specified threshold. This configuration is ignored if log.message.timestamp.type=LogAppendTime."

          public static String LogMessageTimestampAfterMaxMsDoc = "This configuration sets the allowable timestamp difference between the " +
          "message timestamp and the broker's timestamp. The message timestamp can be later than or equal to the broker's " +
          "timestamp, with the maximum allowable difference determined by the value set in this configuration. " +
          "If log.message.timestamp.type=CreateTime, the message will be rejected if the difference in timestamps exceeds " +
          "this specified threshold. This configuration is ignored if log.message.timestamp.type=LogAppendTime."

          public static String NumRecoveryThreadsPerDataDirDoc = "The number of threads per data directory to be used for log recovery at startup and flushing at shutdown";
          public static String AutoCreateTopicsEnableDoc = "Enable auto creation of topic on the server.";
          public static String MinInSyncReplicasDoc = "When a producer sets acks to \"all\" (or \"-1\"), " +
          "<code>min.insync.replicas</code> specifies the minimum number of replicas that must acknowledge " +
          "a write for the write to be considered successful. If this minimum cannot be met, " +
          "then the producer will raise an exception (either <code>NotEnoughReplicas</code> or " +
          "<code>NotEnoughReplicasAfterAppend</code>).<br>When used together, <code>min.insync.replicas</code> and acks " +
          "allow you to enforce greater durability guarantees. A typical scenario would be to " +
          "create a topic with a replication factor of 3, set <code>min.insync.replicas</code> to 2, and " +
          "produce with acks of \"all\". This will ensure that the producer raises an exception " +
          "if a majority of replicas do not receive a write."

          public static String CreateTopicPolicyClassNameDoc = "The create topic policy class that should be used for validation. The class should " +
          "implement the <code>org.apache.kafka.server.policy.CreateTopicPolicy</code> interface."
          public static String AlterConfigPolicyClassNameDoc = "The alter configs policy class that should be used for validation. The class should " +
          "implement the <code>org.apache.kafka.server.policy.AlterConfigPolicy</code> interface."
          public static String LogMessageDownConversionEnableDoc = TopicConfig.MESSAGE_DOWNCONVERSION_ENABLE_DOC;

          /** ********* Replication configuration ***********/
          public static String ControllerSocketTimeoutMsDoc = "The socket timeout for controller-to-broker channels.";
          public static String DefaultReplicationFactorDoc = "The default replication factors for automatically created topics.";
          public static String ReplicaLagTimeMaxMsDoc = "If a follower hasn't sent any fetch requests or hasn't consumed up to the leaders log end offset for at least this time," +
          " the leader will remove the follower from isr"
          public static String ReplicaSocketTimeoutMsDoc = "The socket timeout for network requests. Its value should be at least replica.fetch.wait.max.ms";
          public static String ReplicaSocketReceiveBufferBytesDoc = "The socket receive buffer for network requests to the leader for replicating data";
          public static String ReplicaFetchMaxBytesDoc = "The number of bytes of messages to attempt to fetch for each partition. This is not an absolute maximum, " +
          "if the first record batch in the first non-empty partition of the fetch is larger than this value, the record batch will still be returned " +
          "to ensure that progress can be made. The maximum record batch size accepted by the broker is defined via " +
          "<code>message.max.bytes</code> (broker config) or <code>max.message.bytes</code> (topic config)."
          public static String ReplicaFetchWaitMaxMsDoc = "The maximum wait time for each fetcher request issued by follower replicas. This value should always be less than the " +
          "replica.lag.time.max.ms at all times to prevent frequent shrinking of ISR for low throughput topics"
          public static String ReplicaFetchMinBytesDoc = "Minimum bytes expected for each fetch response. If not enough bytes, wait up to <code>replica.fetch.wait.max.ms</code> (broker config).";
          public static String ReplicaFetchResponseMaxBytesDoc = "Maximum bytes expected for the entire fetch response. Records are fetched in batches, " +
          "and if the first record batch in the first non-empty partition of the fetch is larger than this value, the record batch " +
          "will still be returned to ensure that progress can be made. As such, this is not an absolute maximum. The maximum " +
          "record batch size accepted by the broker is defined via <code>message.max.bytes</code> (broker config) or " +
          "<code>max.message.bytes</code> (topic config)."
          public static String NumReplicaFetchersDoc = "Number of fetcher threads used to replicate records from each source broker. The total number of fetchers " +
          "on each broker is bound by <code>num.replica.fetchers</code> multiplied by the number of brokers in the cluster." +
          "Increasing this value can increase the degree of I/O parallelism in the follower and leader broker at the cost " +
          "of higher CPU and memory utilization."
          public static String ReplicaFetchBackoffMsDoc = "The amount of time to sleep when fetch partition error occurs.";
          public static String ReplicaHighWatermarkCheckpointIntervalMsDoc = "The frequency with which the high watermark is saved out to disk";
          public static String FetchPurgatoryPurgeIntervalRequestsDoc = "The purge interString (in number of requests) of the fetch request purgatory";
          public static String ProducerPurgatoryPurgeIntervalRequestsDoc = "The purge interString (in number of requests) of the producer request purgatory";
          public static String DeleteRecordsPurgatoryPurgeIntervalRequestsDoc = "The purge interString (in number of requests) of the delete records request purgatory";
          public static String AutoLeaderRebalanceEnableDoc = s"Enables auto leader balancing. A background thread checks the distribution of partition leaders at regular intervals, configurable by $LeaderImbalanceCheckIntervalSecondsProp. If the leader imbalance exceeds $LeaderImbalancePerBrokerPercentageProp, leader rebalance to the preferred leader for partitions is triggered.";
          public static String LeaderImbalancePerBrokerPercentageDoc = "The ratio of leader imbalance allowed per broker. The controller would trigger a leader balance if it goes above this value per broker. The value is specified in percentage.";
          public static String LeaderImbalanceCheckIntervalSecondsDoc = "The frequency with which the partition rebalance check is triggered by the controller";
          public static String UncleanLeaderElectionEnableDoc = "Indicates whether to enable replicas not in the ISR set to be elected as leader as a last resort, even though doing so may result in data loss";
          public static String InterBrokerSecurityProtocolDoc = "Security protocol used to communicate between brokers. Valid values are: " +
          s"${SecurityProtocol.names.asScala.mkString(", ")}. It is an error to set this and $InterBrokerListenerNameProp " +
          "properties at the same time."
          public static String InterBrokerProtocolVersionDoc = "Specify which version of the inter-broker protocol will be used.\n" +
          " This is typically bumped after all brokers were upgraded to a new version.\n" +
          " Example of some valid values are: 0.8.0, 0.8.1, 0.8.1.1, 0.8.2, 0.8.2.0, 0.8.2.1, 0.9.0.0, 0.9.0.1 Check MetadataVersion for the full list."
          public static String InterBrokerListenerNameDoc = s"Name of listener used for communication between brokers. If this is unset, the listener name is defined by $InterBrokerSecurityProtocolProp. " +
          s"It is an error to set this and $InterBrokerSecurityProtocolProp properties at the same time."
          public static String ReplicaSelectorClassDoc = "The fully qualified class name that implements ReplicaSelector. This is used by the broker to find the preferred read replica. By default, we use an implementation that returns the leader.";
          /** ********* Controlled shutdown configuration ***********/
          public static String ControlledShutdownMaxRetriesDoc = "Controlled shutdown can fail for multiple reasons. This determines the number of retries when such failure happens";
          public static String ControlledShutdownRetryBackoffMsDoc = "Before each retry, the system needs time to recover from the state that caused the previous failure (Controller fail over, replica lag etc). This config determines the amount of time to wait before retrying.";
          public static String ControlledShutdownEnableDoc = "Enable controlled shutdown of the server.";

          /** ********* Group coordinator configuration ***********/
          public static String GroupMinSessionTimeoutMsDoc = "The minimum allowed session timeout for registered consumers. Shorter timeouts result in quicker failure detection at the cost of more frequent consumer heartbeating, which can overwhelm broker resources.";
          public static String GroupMaxSessionTimeoutMsDoc = "The maximum allowed session timeout for registered consumers. Longer timeouts give consumers more time to process messages in between heartbeats at the cost of a longer time to detect failures.";
          public static String GroupInitialRebalanceDelayMsDoc = "The amount of time the group coordinator will wait for more consumers to join a new group before performing the first rebalance. A longer delay means potentially fewer rebalances, but increases the time until processing begins.";
          public static String GroupMaxSizeDoc = "The maximum number of consumers that a single consumer group can accommodate.";

          /** New group coordinator configs */
          public static String NewGroupCoordinatorEnableDoc = "Enable the new group coordinator.";
          public static String GroupCoordinatorRebalanceProtocolsDoc = "The list of enabled rebalance protocols. Supported protocols: " + Utils.join(GroupType.values.toList.map(_.toString).asJava, ",") + ". " +
          s"The ${GroupType.CONSUMER} rebalance protocol is in early access and therefore must not be used in production."
          public static String GroupCoordinatorNumThreadsDoc = "The number of threads used by the group coordinator.";

          /** Consumer group configs */
          public static String ConsumerGroupSessionTimeoutMsDoc = "The timeout to detect client failures when using the consumer group protocol.";
          public static String ConsumerGroupMinSessionTimeoutMsDoc = "The minimum allowed session timeout for registered consumers.";
          public static String ConsumerGroupMaxSessionTimeoutMsDoc = "The maximum allowed session timeout for registered consumers.";
          public static String ConsumerGroupHeartbeatIntervalMsDoc = "The heartbeat interString given to the members of a consumer group.";
          public static String ConsumerGroupMinHeartbeatIntervalMsDoc = "The minimum heartbeat interString for registered consumers.";
          public static String ConsumerGroupMaxHeartbeatIntervalMsDoc = "The maximum heartbeat interString for registered consumers.";
          public static String ConsumerGroupMaxSizeDoc = "The maximum number of consumers that a single consumer group can accommodate.";
          public static String ConsumerGroupAssignorsDoc = "The server side assignors as a list of full class names. The first one in the list is considered as the default assignor to be used in the case where the consumer does not specify an assignor.";

          /** ********* Offset management configuration ***********/
          public static String OffsetMetadataMaxSizeDoc = "The maximum size for a metadata entry associated with an offset commit.";
          public static String OffsetsLoadBufferSizeDoc = "Batch size for reading from the offsets segments when loading offsets into the cache (soft-limit, overridden if records are too large).";
          public static String OffsetsTopicReplicationFactorDoc = "The replication factor for the offsets topic (set higher to ensure availability). " +
          "Internal topic creation will fail until the cluster size meets this replication factor requirement."
          public static String OffsetsTopicPartitionsDoc = "The number of partitions for the offset commit topic (should not change after deployment).";
          public static String OffsetsTopicSegmentBytesDoc = "The offsets topic segment bytes should be kept relatively small in order to facilitate faster log compaction and cache loads.";
          public static String OffsetsTopicCompressionCodecDoc = "Compression codec for the offsets topic - compression may be used to achieve \"atomic\" commits.";
          public static String OffsetsRetentionMinutesDoc = "For subscribed consumers, committed offset of a specific partition will be expired and discarded when 1) this retention period has elapsed after the consumer group loses all its consumers (i.e. becomes empty); " +
          "2) this retention period has elapsed since the last time an offset is committed for the partition and the group is no longer subscribed to the corresponding topic. " +
          "For standalone consumers (using manual assignment), offsets will be expired after this retention period has elapsed since the time of last commit. " +
          "Note that when a group is deleted via the delete-group request, its committed offsets will also be deleted without extra retention period; " +
          "also when a topic is deleted via the delete-topic request, upon propagated metadata update any group's committed offsets for that topic will also be deleted without extra retention period."
          public static String OffsetsRetentionCheckIntervalMsDoc = "Frequency at which to check for stale offsets";
          public static String OffsetCommitTimeoutMsDoc = "Offset commit will be delayed until all replicas for the offsets topic receive the commit " +
          "or this timeout is reached. This is similar to the producer request timeout."
          public static String OffsetCommitRequiredAcksDoc = "The required acks before the commit can be accepted. In general, the default (-1) should not be overridden.";
          /** ********* Transaction management configuration ***********/
          public static String TransactionalIdExpirationMsDoc = "The time in ms that the transaction coordinator will wait without receiving any transaction status updates " +
          "for the current transaction before expiring its transactional id. Transactional IDs will not expire while a the transaction is still ongoing."
          public static String TransactionsMaxTimeoutMsDoc = "The maximum allowed timeout for transactions. " +
          "If a client’s requested transaction time exceed this, then the broker will return an error in InitProducerIdRequest. This prevents a client from too large of a timeout, which can stall consumers reading from topics included in the transaction."
          public static String TransactionsTopicMinISRDoc = "Overridden " + MinInSyncReplicasProp + " config for the transaction topic.";
          public static String TransactionsLoadBufferSizeDoc = "Batch size for reading from the transaction log segments when loading producer ids and transactions into the cache (soft-limit, overridden if records are too large).";
          public static String TransactionsTopicReplicationFactorDoc = "The replication factor for the transaction topic (set higher to ensure availability). " +
          "Internal topic creation will fail until the cluster size meets this replication factor requirement."
          public static String TransactionsTopicPartitionsDoc = "The number of partitions for the transaction topic (should not change after deployment).";
          public static String TransactionsTopicSegmentBytesDoc = "The transaction topic segment bytes should be kept relatively small in order to facilitate faster log compaction and cache loads";
          public static String TransactionsAbortTimedOutTransactionsIntervalMsDoc = "The interString at which to rollback transactions that have timed out";
          public static String TransactionsRemoveExpiredTransactionsIntervalMsDoc = "The interString at which to remove transactions that have expired due to <code>transactional.id.expiration.ms</code> passing";

          public static String TransactionPartitionVerificationEnableDoc = "Enable verification that checks that the partition has been added to the transaction before writing transactional records to the partition";

          public static String ProducerIdExpirationMsDoc = "The time in ms that a topic partition leader will wait before expiring producer IDs. Producer IDs will not expire while a transaction associated to them is still ongoing. " +
          "Note that producer IDs may expire sooner if the last write from the producer ID is deleted due to the topic's retention settings. Setting this value the same or higher than " +
          "<code>delivery.timeout.ms</code> can help prevent expiration during retries and protect against message duplication, but the default should be reasonable for most use cases."
          public static String ProducerIdExpirationCheckIntervalMsDoc = "The interString at which to remove producer IDs that have expired due to <code>producer.id.expiration.ms</code> passing.";

          /** ********* Fetch Configuration **************/
          public static String MaxIncrementalFetchSessionCacheSlotsDoc = "The maximum number of incremental fetch sessions that we will maintain.";
          public static String FetchMaxBytesDoc = "The maximum number of bytes we will return for a fetch request. Must be at least 1024.";

          /** ********* Request Limit Configuration **************/
          public static String MaxRequestPartitionSizeLimitDoc = "The maximum number of partitions can be served in one request.";

          /** ********* Quota Configuration ***********/
          public static String NumQuotaSamplesDoc = "The number of samples to retain in memory for client quotas";
          public static String NumReplicationQuotaSamplesDoc = "The number of samples to retain in memory for replication quotas";
          public static String NumAlterLogDirsReplicationQuotaSamplesDoc = "The number of samples to retain in memory for alter log dirs replication quotas";
          public static String NumControllerQuotaSamplesDoc = "The number of samples to retain in memory for controller mutation quotas";
          public static String QuotaWindowSizeSecondsDoc = "The time span of each sample for client quotas";
          public static String ReplicationQuotaWindowSizeSecondsDoc = "The time span of each sample for replication quotas";
          public static String AlterLogDirsReplicationQuotaWindowSizeSecondsDoc = "The time span of each sample for alter log dirs replication quotas";
          public static String ControllerQuotaWindowSizeSecondsDoc = "The time span of each sample for controller mutations quotas";

          public static String ClientQuotaCallbackClassDoc = "The fully qualified name of a class that implements the ClientQuotaCallback interface, " +
          "which is used to determine quota limits applied to client requests. By default, the &lt;user&gt; and &lt;client-id&gt; " +
          "quotas that are stored in ZooKeeper are applied. For any given request, the most specific quota that matches the user principal " +
          "of the session and the client-id of the request is applied."

          public static String DeleteTopicEnableDoc = "Enables delete topic. Delete topic through the admin tool will have no effect if this config is turned off";
          public static String CompressionTypeDoc = "Specify the final compression type for a given topic. This configuration accepts the standard compression codecs " +
          "('gzip', 'snappy', 'lz4', 'zstd'). It additionally accepts 'uncompressed' which is equivalent to no compression; and " +
          "'producer' which means retain the original compression codec set by the producer.";

          /** ********* Kafka Metrics Configuration ***********/
          public static String MetricSampleWindowMsDoc = CommonClientConfigs.METRICS_SAMPLE_WINDOW_MS_DOC;
          public static String MetricNumSamplesDoc = CommonClientConfigs.METRICS_NUM_SAMPLES_DOC;
          public static String MetricReporterClassesDoc = CommonClientConfigs.METRIC_REPORTER_CLASSES_DOC;
          public static String MetricRecordingLevelDoc = CommonClientConfigs.METRICS_RECORDING_LEVEL_DOC;
          public static String AutoIncludeJmxReporterDoc = CommonClientConfigs.AUTO_INCLUDE_JMX_REPORTER_DOC;


          /** ********* Kafka Yammer Metrics Reporter Configuration ***********/
          public static String KafkaMetricsReporterClassesDoc = "A list of classes to use as Yammer metrics custom reporters." +
          " The reporters should implement <code>kafka.metrics.KafkaMetricsReporter</code> trait. If a client wants" +
          " to expose JMX operations on a custom reporter, the custom reporter needs to additionally implement an MBean" +
          " trait that extends <code>kafka.metrics.KafkaMetricsReporterMBean</code> trait so that the registered MBean is compliant with" +
          " the standard MBean convention.";

          public static String KafkaMetricsPollingIntervalSecondsDoc = s"The metrics polling interString (in seconds) which can be used" +
          s" in $KafkaMetricsReporterClassesProp implementations.";

          /** ********* Kafka Client Telemetry Metrics Configuration ***********/
          public static String ClientTelemetryMaxBytesDoc = "The maximum size (after compression if compression is used) of" +
          " telemetry metrics pushed from a client to the broker. The default value is 1048576 (1 MB).";

          /** ******** Common Security Configuration *************/
          public static String PrincipalBuilderClassDoc = BrokerSecurityConfigs.PRINCIPAL_BUILDER_CLASS_DOC;
          public static String ConnectionsMaxReauthMsDoc = BrokerSecurityConfigs.CONNECTIONS_MAX_REAUTH_MS_DOC;
          public static String SaslServerMaxReceiveSizeDoc = BrokerSecurityConfigs.SASL_SERVER_MAX_RECEIVE_SIZE_DOC;
          public static String securityProviderClassDoc = SecurityConfig.SECURITY_PROVIDERS_DOC;

          /** ********* SSL Configuration ****************/
          public static String SslProtocolDoc = SslConfigs.SSL_PROTOCOL_DOC;
          public static String SslProviderDoc = SslConfigs.SSL_PROVIDER_DOC;
          public static String SslCipherSuitesDoc = SslConfigs.SSL_CIPHER_SUITES_DOC;
          public static String SslEnabledProtocolsDoc = SslConfigs.SSL_ENABLED_PROTOCOLS_DOC;
          public static String SslKeystoreTypeDoc = SslConfigs.SSL_KEYSTORE_TYPE_DOC;
          public static String SslKeystoreLocationDoc = SslConfigs.SSL_KEYSTORE_LOCATION_DOC;
          public static String SslKeystorePasswordDoc = SslConfigs.SSL_KEYSTORE_PASSWORD_DOC;
          public static String SslKeyPasswordDoc = SslConfigs.SSL_KEY_PASSWORD_DOC;
          public static String SslKeystoreKeyDoc = SslConfigs.SSL_KEYSTORE_KEY_DOC;
          public static String SslKeystoreCertificateChainDoc = SslConfigs.SSL_KEYSTORE_CERTIFICATE_CHAIN_DOC;
          public static String SslTruststoreTypeDoc = SslConfigs.SSL_TRUSTSTORE_TYPE_DOC;
          public static String SslTruststorePasswordDoc = SslConfigs.SSL_TRUSTSTORE_PASSWORD_DOC;
          public static String SslTruststoreLocationDoc = SslConfigs.SSL_TRUSTSTORE_LOCATION_DOC;
          public static String SslTruststoreCertificatesDoc = SslConfigs.SSL_TRUSTSTORE_CERTIFICATES_DOC;
          public static String SslKeyManagerAlgorithmDoc = SslConfigs.SSL_KEYMANAGER_ALGORITHM_DOC;
          public static String SslTrustManagerAlgorithmDoc = SslConfigs.SSL_TRUSTMANAGER_ALGORITHM_DOC;
          public static String SslEndpointIdentificationAlgorithmDoc = SslConfigs.SSL_ENDPOINT_IDENTIFICATION_ALGORITHM_DOC;
          public static String SslSecureRandomImplementationDoc = SslConfigs.SSL_SECURE_RANDOM_IMPLEMENTATION_DOC;
          public static String SslClientAuthDoc = BrokerSecurityConfigs.SSL_CLIENT_AUTH_DOC;
          public static String SslPrincipalMappingRulesDoc = BrokerSecurityConfigs.SSL_PRINCIPAL_MAPPING_RULES_DOC;
          public static String SslEngineFactoryClassDoc = SslConfigs.SSL_ENGINE_FACTORY_CLASS_DOC;
          public static String SslAllowDnChangesDoc = BrokerSecurityConfigs.SSL_ALLOW_DN_CHANGES_DOC;
          public static String SslAllowSanChangesDoc = BrokerSecurityConfigs.SSL_ALLOW_SAN_CHANGES_DOC;

          /** ********* Sasl Configuration ****************/
          public static String SaslMechanismInterBrokerProtocolDoc = "SASL mechanism used for inter-broker communication. Default is GSSAPI.";
          public static String SaslJaasConfigDoc = SaslConfigs.SASL_JAAS_CONFIG_DOC;
          public static String SaslEnabledMechanismsDoc = BrokerSecurityConfigs.SASL_ENABLED_MECHANISMS_DOC;
          public static String SaslServerCallbackHandlerClassDoc = BrokerSecurityConfigs.SASL_SERVER_CALLBACK_HANDLER_CLASS_DOC;
          public static String SaslClientCallbackHandlerClassDoc = SaslConfigs.SASL_CLIENT_CALLBACK_HANDLER_CLASS_DOC;
          public static String SaslLoginClassDoc = SaslConfigs.SASL_LOGIN_CLASS_DOC;
          public static String SaslLoginCallbackHandlerClassDoc = SaslConfigs.SASL_LOGIN_CALLBACK_HANDLER_CLASS_DOC;
          public static String SaslKerberosServiceNameDoc = SaslConfigs.SASL_KERBEROS_SERVICE_NAME_DOC;
          public static String SaslKerberosKinitCmdDoc = SaslConfigs.SASL_KERBEROS_KINIT_CMD_DOC;
          public static String SaslKerberosTicketRenewWindowFactorDoc = SaslConfigs.SASL_KERBEROS_TICKET_RENEW_WINDOW_FACTOR_DOC;
          public static String SaslKerberosTicketRenewJitterDoc = SaslConfigs.SASL_KERBEROS_TICKET_RENEW_JITTER_DOC;
          public static String SaslKerberosMinTimeBeforeReloginDoc = SaslConfigs.SASL_KERBEROS_MIN_TIME_BEFORE_RELOGIN_DOC;
          public static String SaslKerberosPrincipalToLocalRulesDoc = BrokerSecurityConfigs.SASL_KERBEROS_PRINCIPAL_TO_LOCAL_RULES_DOC;
          public static String SaslLoginRefreshWindowFactorDoc = SaslConfigs.SASL_LOGIN_REFRESH_WINDOW_FACTOR_DOC;
          public static String SaslLoginRefreshWindowJitterDoc = SaslConfigs.SASL_LOGIN_REFRESH_WINDOW_JITTER_DOC;
          public static String SaslLoginRefreshMinPeriodSecondsDoc = SaslConfigs.SASL_LOGIN_REFRESH_MIN_PERIOD_SECONDS_DOC;
          public static String SaslLoginRefreshBufferSecondsDoc = SaslConfigs.SASL_LOGIN_REFRESH_BUFFER_SECONDS_DOC;

          public static String SaslLoginConnectTimeoutMsDoc = SaslConfigs.SASL_LOGIN_CONNECT_TIMEOUT_MS_DOC;
          public static String SaslLoginReadTimeoutMsDoc = SaslConfigs.SASL_LOGIN_READ_TIMEOUT_MS_DOC;
          public static String SaslLoginRetryBackoffMaxMsDoc = SaslConfigs.SASL_LOGIN_RETRY_BACKOFF_MAX_MS_DOC;
          public static String SaslLoginRetryBackoffMsDoc = SaslConfigs.SASL_LOGIN_RETRY_BACKOFF_MS_DOC;
          public static String SaslOAuthBearerScopeClaimNameDoc = SaslConfigs.SASL_OAUTHBEARER_SCOPE_CLAIM_NAME_DOC;
          public static String SaslOAuthBearerSubClaimNameDoc = SaslConfigs.SASL_OAUTHBEARER_SUB_CLAIM_NAME_DOC;
          public static String SaslOAuthBearerTokenEndpointUrlDoc = SaslConfigs.SASL_OAUTHBEARER_TOKEN_ENDPOINT_URL_DOC;
          public static String SaslOAuthBearerJwksEndpointUrlDoc = SaslConfigs.SASL_OAUTHBEARER_JWKS_ENDPOINT_URL_DOC;
          public static String SaslOAuthBearerJwksEndpointRefreshMsDoc = SaslConfigs.SASL_OAUTHBEARER_JWKS_ENDPOINT_REFRESH_MS_DOC;
          public static String SaslOAuthBearerJwksEndpointRetryBackoffMaxMsDoc = SaslConfigs.SASL_OAUTHBEARER_JWKS_ENDPOINT_RETRY_BACKOFF_MAX_MS_DOC;
          public static String SaslOAuthBearerJwksEndpointRetryBackoffMsDoc = SaslConfigs.SASL_OAUTHBEARER_JWKS_ENDPOINT_RETRY_BACKOFF_MS_DOC;
          public static String SaslOAuthBearerClockSkewSecondsDoc = SaslConfigs.SASL_OAUTHBEARER_CLOCK_SKEW_SECONDS_DOC;
          public static String SaslOAuthBearerExpectedAudienceDoc = SaslConfigs.SASL_OAUTHBEARER_EXPECTED_AUDIENCE_DOC;
          public static String SaslOAuthBearerExpectedIssuerDoc = SaslConfigs.SASL_OAUTHBEARER_EXPECTED_ISSUER_DOC;

          /** ********* Delegation Token Configuration ****************/
          public static String DelegationTokenSecretKeyAliasDoc = s"DEPRECATED: An alias for $DelegationTokenSecretKeyProp, which should be used instead of this config.";
          public static String DelegationTokenSecretKeyDoc = "Secret key to generate and verify delegation tokens. The same key must be configured across all the brokers. " +
          " If using Kafka with KRaft, the key must also be set across all controllers. " +
          " If the key is not set or set to empty string, brokers will disable the delegation token support.";
          public static String DelegationTokenMaxLifeTimeDoc = "The token has a maximum lifetime beyond which it cannot be renewed anymore. Default value 7 days.";
          public static String DelegationTokenExpiryTimeMsDoc = "The token validity time in milliseconds before the token needs to be renewed. Default value 1 day.";
          public static String DelegationTokenExpiryCheckIntervalDoc = "Scan interString to remove expired delegation tokens.";

          /** ********* Password encryption configuration for dynamic configs *********/
          public static String PasswordEncoderSecretDoc = "The secret used for encoding dynamically configured passwords for this broker.";
          public static String PasswordEncoderOldSecretDoc = "The old secret that was used for encoding dynamically configured passwords. " +
          "This is required only when the secret is updated. If specified, all dynamically encoded passwords are " +
          s"decoded using this old secret and re-encoded using $PasswordEncoderSecretProp when broker starts up.";
          public static String PasswordEncoderKeyFactoryAlgorithmDoc = "The SecretKeyFactory algorithm used for encoding dynamically configured passwords. " +
          "Default is PBKDF2WithHmacSHA512 if available and PBKDF2WithHmacSHA1 otherwise.";
          public static String PasswordEncoderCipherAlgorithmDoc = "The Cipher algorithm used for encoding dynamically configured passwords.";
          public static String PasswordEncoderKeyLengthDoc =  "The key length used for encoding dynamically configured passwords.";
          public static String PasswordEncoderIterationsDoc =  "The iteration count used for encoding dynamically configured passwords.";

    @SuppressWarnings("deprecation")
    ConfigDef configDef =
          new ConfigDef()

          /** ********* Zookeeper Configuration ***********/
          .define(ZkConnectProp, STRING, null, HIGH, ZkConnectDoc)
          .define(ZkSessionTimeoutMsProp, INT, Defaults.ZK_SESSION_TIMEOUT_MS, HIGH, ZkSessionTimeoutMsDoc)
          .define(ZkConnectionTimeoutMsProp, INT, null, HIGH, ZkConnectionTimeoutMsDoc)
          .define(ZkEnableSecureAclsProp, BOOLEAN, Defaults.ZK_ENABLE_SECURE_ACLS, HIGH, ZkEnableSecureAclsDoc)
          .define(ZkMaxInFlightRequestsProp, INT, Defaults.ZK_MAX_IN_FLIGHT_REQUESTS, atLeast(1), HIGH, ZkMaxInFlightRequestsDoc)
          .define(ZkSslClientEnableProp, BOOLEAN, Defaults.ZK_SSL_CLIENT_ENABLE, MEDIUM, ZkSslClientEnableDoc)
          .define(ZkClientCnxnSocketProp, STRING, null, MEDIUM, ZkClientCnxnSocketDoc)
          .define(ZkSslKeyStoreLocationProp, STRING, null, MEDIUM, ZkSslKeyStoreLocationDoc)
          .define(ZkSslKeyStorePasswordProp, PASSWORD, null, MEDIUM, ZkSslKeyStorePasswordDoc)
          .define(ZkSslKeyStoreTypeProp, STRING, null, MEDIUM, ZkSslKeyStoreTypeDoc)
          .define(ZkSslTrustStoreLocationProp, STRING, null, MEDIUM, ZkSslTrustStoreLocationDoc)
          .define(ZkSslTrustStorePasswordProp, PASSWORD, null, MEDIUM, ZkSslTrustStorePasswordDoc)
          .define(ZkSslTrustStoreTypeProp, STRING, null, MEDIUM, ZkSslTrustStoreTypeDoc)
          .define(ZkSslProtocolProp, STRING, Defaults.ZK_SSL_PROTOCOL, LOW, ZkSslProtocolDoc)
          .define(ZkSslEnabledProtocolsProp, LIST, null, LOW, ZkSslEnabledProtocolsDoc)
          .define(ZkSslCipherSuitesProp, LIST, null, LOW, ZkSslCipherSuitesDoc)
          .define(ZkSslEndpointIdentificationAlgorithmProp, STRING, Defaults.ZK_SSL_ENDPOINT_IDENTIFICATION_ALGORITHM, LOW, ZkSslEndpointIdentificationAlgorithmDoc)
          .define(ZkSslCrlEnableProp, BOOLEAN, Defaults.ZK_SSL_CRL_ENABLE, LOW, ZkSslCrlEnableDoc)
          .define(ZkSslOcspEnableProp, BOOLEAN, Defaults.ZK_SSL_OCSP_ENABLE, LOW, ZkSslOcspEnableDoc)

          /** ********* General Configuration ***********/
          .define(BrokerIdGenerationEnableProp, BOOLEAN, Defaults.BROKER_ID_GENERATION_ENABLE, MEDIUM, BrokerIdGenerationEnableDoc)
          .define(MaxReservedBrokerIdProp, INT, Defaults.MAX_RESERVED_BROKER_ID, atLeast(0), MEDIUM, MaxReservedBrokerIdDoc)
          .define(BrokerIdProp, INT, Defaults.BROKER_ID, HIGH, BrokerIdDoc)
          .define(MessageMaxBytesProp, INT, LogConfig.DEFAULT_MAX_MESSAGE_BYTES, atLeast(0), HIGH, MessageMaxBytesDoc)
          .define(NumNetworkThreadsProp, INT, Defaults.NUM_NETWORK_THREADS, atLeast(1), HIGH, NumNetworkThreadsDoc)
          .define(NumIoThreadsProp, INT, Defaults.NUM_IO_THREADS, atLeast(1), HIGH, NumIoThreadsDoc)
          .define(NumReplicaAlterLogDirsThreadsProp, INT, null, HIGH, NumReplicaAlterLogDirsThreadsDoc)
          .define(BackgroundThreadsProp, INT, Defaults.BACKGROUND_THREADS, atLeast(1), HIGH, BackgroundThreadsDoc)
          .define(QueuedMaxRequestsProp, INT, Defaults.QUEUED_MAX_REQUESTS, atLeast(1), HIGH, QueuedMaxRequestsDoc)
          .define(QueuedMaxBytesProp, LONG, Defaults.QUEUED_MAX_REQUEST_BYTES, MEDIUM, QueuedMaxRequestBytesDoc)
          .define(RequestTimeoutMsProp, INT, Defaults.REQUEST_TIMEOUT_MS, HIGH, RequestTimeoutMsDoc)
          .define(ConnectionSetupTimeoutMsProp, LONG, Defaults.CONNECTION_SETUP_TIMEOUT_MS, MEDIUM, ConnectionSetupTimeoutMsDoc)
          .define(ConnectionSetupTimeoutMaxMsProp, LONG, Defaults.CONNECTION_SETUP_TIMEOUT_MAX_MS, MEDIUM, ConnectionSetupTimeoutMaxMsDoc)

          /*
           * KRaft mode configs.
           */
          .define(MetadataSnapshotMaxNewRecordBytesProp, LONG, Defaults.METADATA_SNAPSHOT_MAX_NEW_RECORD_BYTES, atLeast(1), HIGH, MetadataSnapshotMaxNewRecordBytesDoc)
          .define(MetadataSnapshotMaxIntervalMsProp, LONG, Defaults.METADATA_SNAPSHOT_MAX_INTERVAL_MS, atLeast(0), HIGH, MetadataSnapshotMaxIntervalMsDoc)
          .define(ProcessRolesProp, LIST, Collections.emptyList(), ValidList.in("broker", "controller"), HIGH, ProcessRolesDoc)
          .define(NodeIdProp, INT, Defaults.EMPTY_NODE_ID, null, HIGH, NodeIdDoc)
          .define(InitialBrokerRegistrationTimeoutMsProp, INT, Defaults.INITIAL_BROKER_REGISTRATION_TIMEOUT_MS, null, MEDIUM, InitialBrokerRegistrationTimeoutMsDoc)
          .define(BrokerHeartbeatIntervalMsProp, INT, Defaults.BROKER_HEARTBEAT_INTERVAL_MS, null, MEDIUM, BrokerHeartbeatIntervalMsDoc)
          .define(BrokerSessionTimeoutMsProp, INT, Defaults.BROKER_SESSION_TIMEOUT_MS, null, MEDIUM, BrokerSessionTimeoutMsDoc)
          .define(ControllerListenerNamesProp, STRING, null, null, HIGH, ControllerListenerNamesDoc)
          .define(SaslMechanismControllerProtocolProp, STRING, SaslConfigs.DEFAULT_SASL_MECHANISM, null, HIGH, SaslMechanismControllerProtocolDoc)
          .define(MetadataLogDirProp, STRING, null, null, HIGH, MetadataLogDirDoc)
          .define(MetadataLogSegmentBytesProp, INT, LogConfig.DEFAULT_SEGMENT_BYTES, atLeast(Records.LOG_OVERHEAD), HIGH, MetadataLogSegmentBytesDoc)
          .defineInternal(MetadataLogSegmentMinBytesProp, INT, 8 * 1024 * 1024, atLeast(Records.LOG_OVERHEAD), HIGH, MetadataLogSegmentMinBytesDoc)
          .define(MetadataLogSegmentMillisProp, LONG, LogConfig.DEFAULT_SEGMENT_MS, null, HIGH, MetadataLogSegmentMillisDoc)
          .define(MetadataMaxRetentionBytesProp, LONG, Defaults.METADATA_MAX_RETENTION_BYTES, null, HIGH, MetadataMaxRetentionBytesDoc)
          .define(MetadataMaxRetentionMillisProp, LONG, LogConfig.DEFAULT_RETENTION_MS, null, HIGH, MetadataMaxRetentionMillisDoc)
          .define(MetadataMaxIdleIntervalMsProp, INT, Defaults.METADATA_MAX_IDLE_INTERVAL_MS, atLeast(0), LOW, MetadataMaxIdleIntervalMsDoc)
          .defineInternal(ServerMaxStartupTimeMsProp, LONG, Defaults.SERVER_MAX_STARTUP_TIME_MS, atLeast(0), MEDIUM, ServerMaxStartupTimeMsDoc)
          .define(MigrationEnabledProp, BOOLEAN, false, HIGH, "Enable ZK to KRaft migration")
          .define(ElrEnabledProp, BOOLEAN, false, HIGH, "Enable the Eligible leader replicas")

          /************* Authorizer Configuration ***********/
          .define(AuthorizerClassNameProp, STRING, Defaults.AUTHORIZER_CLASS_NAME, new ConfigDef.NonNullValidator(), LOW, AuthorizerClassNameDoc)
          .define(EarlyStartListenersProp, STRING, null,  HIGH, EarlyStartListenersDoc)

          /** ********* Socket Server Configuration ***********/
          .define(ListenersProp, STRING, Defaults.LISTENERS, HIGH, ListenersDoc)
          .define(AdvertisedListenersProp, STRING, null, HIGH, AdvertisedListenersDoc)
          .define(ListenerSecurityProtocolMapProp, STRING, Defaults.LISTENER_SECURITY_PROTOCOL_MAP, LOW, ListenerSecurityProtocolMapDoc)
          .define(ControlPlaneListenerNameProp, STRING, null, HIGH, controlPlaneListenerNameDoc)
          .define(SocketSendBufferBytesProp, INT, Defaults.SOCKET_SEND_BUFFER_BYTES, HIGH, SocketSendBufferBytesDoc)
          .define(SocketReceiveBufferBytesProp, INT, Defaults.SOCKET_RECEIVE_BUFFER_BYTES, HIGH, SocketReceiveBufferBytesDoc)
          .define(SocketRequestMaxBytesProp, INT, Defaults.SOCKET_REQUEST_MAX_BYTES, atLeast(1), HIGH, SocketRequestMaxBytesDoc)
          .define(SocketListenBacklogSizeProp, INT, Defaults.SOCKET_LISTEN_BACKLOG_SIZE, atLeast(1), MEDIUM, SocketListenBacklogSizeDoc)
          .define(MaxConnectionsPerIpProp, INT, Defaults.MAX_CONNECTIONS_PER_IP, atLeast(0), MEDIUM, MaxConnectionsPerIpDoc)
          .define(MaxConnectionsPerIpOverridesProp, STRING, Defaults.MAX_CONNECTIONS_PER_IP_OVERRIDES, MEDIUM, MaxConnectionsPerIpOverridesDoc)
          .define(MaxConnectionsProp, INT, Defaults.MAX_CONNECTIONS, atLeast(0), MEDIUM, MaxConnectionsDoc)
          .define(MaxConnectionCreationRateProp, INT, Defaults.MAX_CONNECTION_CREATION_RATE, atLeast(0), MEDIUM, MaxConnectionCreationRateDoc)
          .define(ConnectionsMaxIdleMsProp, LONG, Defaults.CONNECTIONS_MAX_IDLE_MS, MEDIUM, ConnectionsMaxIdleMsDoc)
          .define(FailedAuthenticationDelayMsProp, INT, Defaults.FAILED_AUTHENTICATION_DELAY_MS, atLeast(0), LOW, FailedAuthenticationDelayMsDoc)

          /************ Rack Configuration ******************/
          .define(RackProp, STRING, null, MEDIUM, RackDoc)

          /** ********* Log Configuration ***********/
          .define(NumPartitionsProp, INT, Defaults.NUM_PARTITIONS, atLeast(1), MEDIUM, NumPartitionsDoc)
          .define(LogDirProp, STRING, Defaults.LOG_DIR, HIGH, LogDirDoc)
          .define(LogDirsProp, STRING, null, HIGH, LogDirsDoc)
          .define(LogSegmentBytesProp, INT, LogConfig.DEFAULT_SEGMENT_BYTES, atLeast(LegacyRecord.RECORD_OVERHEAD_V0), HIGH, LogSegmentBytesDoc)

          .define(LogRollTimeMillisProp, LONG, null, HIGH, LogRollTimeMillisDoc)
          .define(LogRollTimeHoursProp, INT, (int) TimeUnit.MILLISECONDS.toHours(LogConfig.DEFAULT_SEGMENT_MS), atLeast(1), HIGH, LogRollTimeHoursDoc)

          .define(LogRollTimeJitterMillisProp, LONG, null, HIGH, LogRollTimeJitterMillisDoc)
          .define(LogRollTimeJitterHoursProp, INT, (int) TimeUnit.MILLISECONDS.toHours(LogConfig.DEFAULT_SEGMENT_JITTER_MS), atLeast(0), HIGH, LogRollTimeJitterHoursDoc)

          .define(LogRetentionTimeMillisProp, LONG, null, HIGH, LogRetentionTimeMillisDoc)
          .define(LogRetentionTimeMinutesProp, INT, null, HIGH, LogRetentionTimeMinsDoc)
          .define(LogRetentionTimeHoursProp, INT, (int) TimeUnit.MILLISECONDS.toHours(LogConfig.DEFAULT_RETENTION_MS), HIGH, LogRetentionTimeHoursDoc)

          .define(LogRetentionBytesProp, LONG, LogConfig.DEFAULT_RETENTION_BYTES, HIGH, LogRetentionBytesDoc)
          .define(LogCleanupIntervalMsProp, LONG, Defaults.LOG_CLEANUP_INTERVAL_MS, atLeast(1), MEDIUM, LogCleanupIntervalMsDoc)
          .define(LogCleanupPolicyProp, LIST, LogConfig.DEFAULT_CLEANUP_POLICY, ValidList.in(TopicConfig.CLEANUP_POLICY_COMPACT, TopicConfig.CLEANUP_POLICY_DELETE), MEDIUM, LogCleanupPolicyDoc)
          .define(LogCleanerThreadsProp, INT, Defaults.LOG_CLEANER_THREADS, atLeast(0), MEDIUM, LogCleanerThreadsDoc)
          .define(LogCleanerIoMaxBytesPerSecondProp, DOUBLE, Defaults.LOG_CLEANER_IO_MAX_BYTES_PER_SECOND, MEDIUM, LogCleanerIoMaxBytesPerSecondDoc)
          .define(LogCleanerDedupeBufferSizeProp, LONG, Defaults.LOG_CLEANER_DEDUPE_BUFFER_SIZE, MEDIUM, LogCleanerDedupeBufferSizeDoc)
          .define(LogCleanerIoBufferSizeProp, INT, Defaults.LOG_CLEANER_IO_BUFFER_SIZE, atLeast(0), MEDIUM, LogCleanerIoBufferSizeDoc)
          .define(LogCleanerDedupeBufferLoadFactorProp, DOUBLE, Defaults.LOG_CLEANER_DEDUPE_BUFFER_LOAD_FACTOR, MEDIUM, LogCleanerDedupeBufferLoadFactorDoc)
          .define(LogCleanerBackoffMsProp, LONG, Defaults.LOG_CLEANER_BACKOFF_MS, atLeast(0), MEDIUM, LogCleanerBackoffMsDoc)
          .define(LogCleanerMinCleanRatioProp, DOUBLE, LogConfig.DEFAULT_MIN_CLEANABLE_DIRTY_RATIO, between(0, 1), MEDIUM, LogCleanerMinCleanRatioDoc)
          .define(LogCleanerEnableProp, BOOLEAN, Defaults.LOG_CLEANER_ENABLE, MEDIUM, LogCleanerEnableDoc)
          .define(LogCleanerDeleteRetentionMsProp, LONG, LogConfig.DEFAULT_DELETE_RETENTION_MS, atLeast(0), MEDIUM, LogCleanerDeleteRetentionMsDoc)
          .define(LogCleanerMinCompactionLagMsProp, LONG, LogConfig.DEFAULT_MIN_COMPACTION_LAG_MS, atLeast(0), MEDIUM, LogCleanerMinCompactionLagMsDoc)
          .define(LogCleanerMaxCompactionLagMsProp, LONG, LogConfig.DEFAULT_MAX_COMPACTION_LAG_MS, atLeast(1), MEDIUM, LogCleanerMaxCompactionLagMsDoc)
          .define(LogIndexSizeMaxBytesProp, INT, LogConfig.DEFAULT_SEGMENT_INDEX_BYTES, atLeast(4), MEDIUM, LogIndexSizeMaxBytesDoc)
          .define(LogIndexIntervalBytesProp, INT, LogConfig.DEFAULT_INDEX_INTERVAL_BYTES, atLeast(0), MEDIUM, LogIndexIntervalBytesDoc)
          .define(LogFlushIntervalMessagesProp, LONG, LogConfig.DEFAULT_FLUSH_MESSAGES_INTERVAL, atLeast(1), HIGH, LogFlushIntervalMessagesDoc)
          .define(LogDeleteDelayMsProp, LONG, LogConfig.DEFAULT_FILE_DELETE_DELAY_MS, atLeast(0), HIGH, LogDeleteDelayMsDoc)
          .define(LogFlushSchedulerIntervalMsProp, LONG, LogConfig.DEFAULT_FLUSH_MS, HIGH, LogFlushSchedulerIntervalMsDoc)
          .define(LogFlushIntervalMsProp, LONG, null, HIGH, LogFlushIntervalMsDoc)
          .define(LogFlushOffsetCheckpointIntervalMsProp, INT, Defaults.LOG_FLUSH_OFFSET_CHECKPOINT_INTERVAL_MS, atLeast(0), HIGH, LogFlushOffsetCheckpointIntervalMsDoc)
          .define(LogFlushStartOffsetCheckpointIntervalMsProp, INT, Defaults.LOG_FLUSH_START_OFFSET_CHECKPOINT_INTERVAL_MS, atLeast(0), HIGH, LogFlushStartOffsetCheckpointIntervalMsDoc)
          .define(LogPreAllocateProp, BOOLEAN, LogConfig.DEFAULT_PREALLOCATE, MEDIUM, LogPreAllocateEnableDoc)
          .define(NumRecoveryThreadsPerDataDirProp, INT, Defaults.NUM_RECOVERY_THREADS_PER_DATA_DIR, atLeast(1), HIGH, NumRecoveryThreadsPerDataDirDoc)
          .define(AutoCreateTopicsEnableProp, BOOLEAN, Defaults.AUTO_CREATE_TOPICS_ENABLE, HIGH, AutoCreateTopicsEnableDoc)
          .define(MinInSyncReplicasProp, INT, LogConfig.DEFAULT_MIN_IN_SYNC_REPLICAS, atLeast(1), HIGH, MinInSyncReplicasDoc)
          .define(LogMessageFormatVersionProp, STRING, LogConfig.DEFAULT_MESSAGE_FORMAT_VERSION, new MetadataVersionValidator(), MEDIUM, LogMessageFormatVersionDoc)
          .define(LogMessageTimestampTypeProp, STRING, LogConfig.DEFAULT_MESSAGE_TIMESTAMP_TYPE, ValidString.in("CreateTime", "LogAppendTime"), MEDIUM, LogMessageTimestampTypeDoc)
          .define(LogMessageTimestampDifferenceMaxMsProp, LONG, LogConfig.DEFAULT_MESSAGE_TIMESTAMP_DIFFERENCE_MAX_MS, atLeast(0), MEDIUM, LogMessageTimestampDifferenceMaxMsDoc)
          .define(LogMessageTimestampBeforeMaxMsProp, LONG, LogConfig.DEFAULT_MESSAGE_TIMESTAMP_BEFORE_MAX_MS, atLeast(0), MEDIUM, LogMessageTimestampBeforeMaxMsDoc)
          .define(LogMessageTimestampAfterMaxMsProp, LONG, LogConfig.DEFAULT_MESSAGE_TIMESTAMP_AFTER_MAX_MS, atLeast(0), MEDIUM, LogMessageTimestampAfterMaxMsDoc)
          .define(CreateTopicPolicyClassNameProp, CLASS, null, LOW, CreateTopicPolicyClassNameDoc)
          .define(AlterConfigPolicyClassNameProp, CLASS, null, LOW, AlterConfigPolicyClassNameDoc)
          .define(LogMessageDownConversionEnableProp, BOOLEAN, LogConfig.DEFAULT_MESSAGE_DOWNCONVERSION_ENABLE, LOW, LogMessageDownConversionEnableDoc)

          /** ********* Replication configuration ***********/
          .define(ControllerSocketTimeoutMsProp, INT, Defaults.CONTROLLER_SOCKET_TIMEOUT_MS, MEDIUM, ControllerSocketTimeoutMsDoc)
          .define(DefaultReplicationFactorProp, INT, Defaults.REPLICATION_FACTOR, MEDIUM, DefaultReplicationFactorDoc)
          .define(ReplicaLagTimeMaxMsProp, LONG, Defaults.REPLICA_LAG_TIME_MAX_MS, HIGH, ReplicaLagTimeMaxMsDoc)
          .define(ReplicaSocketTimeoutMsProp, INT, Defaults.REPLICA_SOCKET_TIMEOUT_MS, HIGH, ReplicaSocketTimeoutMsDoc)
          .define(ReplicaSocketReceiveBufferBytesProp, INT, Defaults.REPLICA_SOCKET_RECEIVE_BUFFER_BYTES, HIGH, ReplicaSocketReceiveBufferBytesDoc)
          .define(ReplicaFetchMaxBytesProp, INT, Defaults.REPLICA_FETCH_MAX_BYTES, atLeast(0), MEDIUM, ReplicaFetchMaxBytesDoc)
          .define(ReplicaFetchWaitMaxMsProp, INT, Defaults.REPLICA_FETCH_WAIT_MAX_MS, HIGH, ReplicaFetchWaitMaxMsDoc)
          .define(ReplicaFetchBackoffMsProp, INT, Defaults.REPLICA_FETCH_BACKOFF_MS, atLeast(0), MEDIUM, ReplicaFetchBackoffMsDoc)
          .define(ReplicaFetchMinBytesProp, INT, Defaults.REPLICA_FETCH_MIN_BYTES, HIGH, ReplicaFetchMinBytesDoc)
          .define(ReplicaFetchResponseMaxBytesProp, INT, Defaults.REPLICA_FETCH_RESPONSE_MAX_BYTES, atLeast(0), MEDIUM, ReplicaFetchResponseMaxBytesDoc)
          .define(NumReplicaFetchersProp, INT, Defaults.NUM_REPLICA_FETCHERS, HIGH, NumReplicaFetchersDoc)
          .define(ReplicaHighWatermarkCheckpointIntervalMsProp, LONG, Defaults.REPLICA_HIGH_WATERMARK_CHECKPOINT_INTERVAL_MS, HIGH, ReplicaHighWatermarkCheckpointIntervalMsDoc)
          .define(FetchPurgatoryPurgeIntervalRequestsProp, INT, Defaults.FETCH_PURGATORY_PURGE_INTERVAL_REQUESTS, MEDIUM, FetchPurgatoryPurgeIntervalRequestsDoc)
          .define(ProducerPurgatoryPurgeIntervalRequestsProp, INT, Defaults.PRODUCER_PURGATORY_PURGE_INTERVAL_REQUESTS, MEDIUM, ProducerPurgatoryPurgeIntervalRequestsDoc)
          .define(DeleteRecordsPurgatoryPurgeIntervalRequestsProp, INT, Defaults.DELETE_RECORDS_PURGATORY_PURGE_INTERVAL_REQUESTS, MEDIUM, DeleteRecordsPurgatoryPurgeIntervalRequestsDoc)
          .define(AutoLeaderRebalanceEnableProp, BOOLEAN, Defaults.AUTO_LEADER_REBALANCE_ENABLE, HIGH, AutoLeaderRebalanceEnableDoc)
          .define(LeaderImbalancePerBrokerPercentageProp, INT, Defaults.LEADER_IMBALANCE_PER_BROKER_PERCENTAGE, HIGH, LeaderImbalancePerBrokerPercentageDoc)
          .define(LeaderImbalanceCheckIntervalSecondsProp, LONG, Defaults.LEADER_IMBALANCE_CHECK_INTERVAL_SECONDS, atLeast(1), HIGH, LeaderImbalanceCheckIntervalSecondsDoc)
          .define(UncleanLeaderElectionEnableProp, BOOLEAN, LogConfig.DEFAULT_UNCLEAN_LEADER_ELECTION_ENABLE, HIGH, UncleanLeaderElectionEnableDoc)
          .define(InterBrokerSecurityProtocolProp, STRING, Defaults.INTER_BROKER_SECURITY_PROTOCOL, ValidString.in(Utils.enumOptions(SecurityProtocol.class)), MEDIUM, InterBrokerSecurityProtocolDoc)
          .define(InterBrokerProtocolVersionProp, STRING, Defaults.INTER_BROKER_PROTOCOL_VERSION, new MetadataVersionValidator(), MEDIUM, InterBrokerProtocolVersionDoc)
          .define(InterBrokerListenerNameProp, STRING, null, MEDIUM, InterBrokerListenerNameDoc)
          .define(ReplicaSelectorClassProp, STRING, null, MEDIUM, ReplicaSelectorClassDoc)

          /** ********* Controlled shutdown configuration ***********/
          .define(ControlledShutdownMaxRetriesProp, INT, Defaults.CONTROLLED_SHUTDOWN_MAX_RETRIES, MEDIUM, ControlledShutdownMaxRetriesDoc)
          .define(ControlledShutdownRetryBackoffMsProp, LONG, Defaults.CONTROLLED_SHUTDOWN_RETRY_BACKOFF_MS, MEDIUM, ControlledShutdownRetryBackoffMsDoc)
          .define(ControlledShutdownEnableProp, BOOLEAN, Defaults.CONTROLLED_SHUTDOWN_ENABLE, MEDIUM, ControlledShutdownEnableDoc)

          /** ********* Group coordinator configuration ***********/
          .define(GroupMinSessionTimeoutMsProp, INT, Defaults.GROUP_MIN_SESSION_TIMEOUT_MS, MEDIUM, GroupMinSessionTimeoutMsDoc)
          .define(GroupMaxSessionTimeoutMsProp, INT, Defaults.GROUP_MAX_SESSION_TIMEOUT_MS, MEDIUM, GroupMaxSessionTimeoutMsDoc)
          .define(GroupInitialRebalanceDelayMsProp, INT, Defaults.GROUP_INITIAL_REBALANCE_DELAY_MS, MEDIUM, GroupInitialRebalanceDelayMsDoc)
          .define(GroupMaxSizeProp, INT, Defaults.GROUP_MAX_SIZE, atLeast(1), MEDIUM, GroupMaxSizeDoc)

          /** New group coordinator configs */
          .define(GroupCoordinatorRebalanceProtocolsProp, LIST, Defaults.GROUP_COORDINATOR_REBALANCE_PROTOCOLS, ValidList.in(Utils.enumOptions(GroupType.class)), MEDIUM, GroupCoordinatorRebalanceProtocolsDoc)
          .define(GroupCoordinatorNumThreadsProp, INT, Defaults.GROUP_COORDINATOR_NUM_THREADS, atLeast(1), MEDIUM, GroupCoordinatorNumThreadsDoc)
          // Internal configuration used by integration and system tests.
          .defineInternal(NewGroupCoordinatorEnableProp, BOOLEAN, Defaults.NEW_GROUP_COORDINATOR_ENABLE, null, MEDIUM, NewGroupCoordinatorEnableDoc)

          /** Consumer groups configs */
          .define(ConsumerGroupSessionTimeoutMsProp, INT, Defaults.CONSUMER_GROUP_SESSION_TIMEOUT_MS, atLeast(1), MEDIUM, ConsumerGroupSessionTimeoutMsDoc)
          .define(ConsumerGroupMinSessionTimeoutMsProp, INT, Defaults.CONSUMER_GROUP_MIN_SESSION_TIMEOUT_MS, atLeast(1), MEDIUM, ConsumerGroupMinSessionTimeoutMsDoc)
          .define(ConsumerGroupMaxSessionTimeoutMsProp, INT, Defaults.CONSUMER_GROUP_MAX_SESSION_TIMEOUT_MS, atLeast(1), MEDIUM, ConsumerGroupMaxSessionTimeoutMsDoc)
          .define(ConsumerGroupHeartbeatIntervalMsProp, INT, Defaults.CONSUMER_GROUP_HEARTBEAT_INTERVAL_MS, atLeast(1), MEDIUM, ConsumerGroupHeartbeatIntervalMsDoc)
          .define(ConsumerGroupMinHeartbeatIntervalMsProp, INT, Defaults.CONSUMER_GROUP_MIN_HEARTBEAT_INTERVAL_MS, atLeast(1), MEDIUM, ConsumerGroupMinHeartbeatIntervalMsDoc)
          .define(ConsumerGroupMaxHeartbeatIntervalMsProp, INT, Defaults.CONSUMER_GROUP_MAX_HEARTBEAT_INTERVAL_MS, atLeast(1), MEDIUM, ConsumerGroupMaxHeartbeatIntervalMsDoc)
          .define(ConsumerGroupMaxSizeProp, INT, Defaults.CONSUMER_GROUP_MAX_SIZE, atLeast(1), MEDIUM, ConsumerGroupMaxSizeDoc)
          .define(ConsumerGroupAssignorsProp, LIST, Defaults.CONSUMER_GROUP_ASSIGNORS, null, MEDIUM, ConsumerGroupAssignorsDoc)

          /** ********* Offset management configuration ***********/
          .define(OffsetMetadataMaxSizeProp, INT, Defaults.OFFSET_METADATA_MAX_SIZE, HIGH, OffsetMetadataMaxSizeDoc)
          .define(OffsetsLoadBufferSizeProp, INT, Defaults.OFFSETS_LOAD_BUFFER_SIZE, atLeast(1), HIGH, OffsetsLoadBufferSizeDoc)
          .define(OffsetsTopicReplicationFactorProp, SHORT, Defaults.OFFSETS_TOPIC_REPLICATION_FACTOR, atLeast(1), HIGH, OffsetsTopicReplicationFactorDoc)
          .define(OffsetsTopicPartitionsProp, INT, Defaults.OFFSETS_TOPIC_PARTITIONS, atLeast(1), HIGH, OffsetsTopicPartitionsDoc)
          .define(OffsetsTopicSegmentBytesProp, INT, Defaults.OFFSETS_TOPIC_SEGMENT_BYTES, atLeast(1), HIGH, OffsetsTopicSegmentBytesDoc)
          .define(OffsetsTopicCompressionCodecProp, INT, Defaults.OFFSETS_TOPIC_COMPRESSION_CODEC, HIGH, OffsetsTopicCompressionCodecDoc)
          .define(OffsetsRetentionMinutesProp, INT, Defaults.OFFSETS_RETENTION_MINUTES, atLeast(1), HIGH, OffsetsRetentionMinutesDoc)
          .define(OffsetsRetentionCheckIntervalMsProp, LONG, Defaults.OFFSETS_RETENTION_CHECK_INTERVAL_MS, atLeast(1), HIGH, OffsetsRetentionCheckIntervalMsDoc)
          .define(OffsetCommitTimeoutMsProp, INT, Defaults.OFFSET_COMMIT_TIMEOUT_MS, atLeast(1), HIGH, OffsetCommitTimeoutMsDoc)
          .define(OffsetCommitRequiredAcksProp, SHORT, Defaults.OFFSET_COMMIT_REQUIRED_ACKS, HIGH, OffsetCommitRequiredAcksDoc)
          .define(DeleteTopicEnableProp, BOOLEAN, Defaults.DELETE_TOPIC_ENABLE, HIGH, DeleteTopicEnableDoc)
          .define(CompressionTypeProp, STRING, LogConfig.DEFAULT_COMPRESSION_TYPE, ValidString.in(BrokerCompressionType.names().toArray(new String[0])), HIGH, CompressionTypeDoc)

          /** ********* Transaction management configuration ***********/
          .define(TransactionalIdExpirationMsProp, INT, Defaults.TRANSACTIONAL_ID_EXPIRATION_MS, atLeast(1), HIGH, TransactionalIdExpirationMsDoc)
          .define(TransactionsMaxTimeoutMsProp, INT, Defaults.TRANSACTIONS_MAX_TIMEOUT_MS, atLeast(1), HIGH, TransactionsMaxTimeoutMsDoc)
          .define(TransactionsTopicMinISRProp, INT, Defaults.TRANSACTIONS_TOPIC_MIN_ISR, atLeast(1), HIGH, TransactionsTopicMinISRDoc)
          .define(TransactionsLoadBufferSizeProp, INT, Defaults.TRANSACTIONS_LOAD_BUFFER_SIZE, atLeast(1), HIGH, TransactionsLoadBufferSizeDoc)
          .define(TransactionsTopicReplicationFactorProp, SHORT, Defaults.TRANSACTIONS_TOPIC_REPLICATION_FACTOR, atLeast(1), HIGH, TransactionsTopicReplicationFactorDoc)
          .define(TransactionsTopicPartitionsProp, INT, Defaults.TRANSACTIONS_TOPIC_PARTITIONS, atLeast(1), HIGH, TransactionsTopicPartitionsDoc)
          .define(TransactionsTopicSegmentBytesProp, INT, Defaults.TRANSACTIONS_TOPIC_SEGMENT_BYTES, atLeast(1), HIGH, TransactionsTopicSegmentBytesDoc)
          .define(TransactionsAbortTimedOutTransactionCleanupIntervalMsProp, INT, Defaults.TRANSACTIONS_ABORT_TIMED_OUT_CLEANUP_INTERVAL_MS, atLeast(1), LOW, TransactionsAbortTimedOutTransactionsIntervalMsDoc)
          .define(TransactionsRemoveExpiredTransactionalIdCleanupIntervalMsProp, INT, Defaults.TRANSACTIONS_REMOVE_EXPIRED_CLEANUP_INTERVAL_MS, atLeast(1), LOW, TransactionsRemoveExpiredTransactionsIntervalMsDoc)

          .define(TransactionPartitionVerificationEnableProp, BOOLEAN, Defaults.TRANSACTION_PARTITION_VERIFICATION_ENABLE, LOW, TransactionPartitionVerificationEnableDoc)

          .define(ProducerIdExpirationMsProp, INT, Defaults.PRODUCER_ID_EXPIRATION_MS, atLeast(1), LOW, ProducerIdExpirationMsDoc)
          // Configuration for testing only as default value should be sufficient for typical usage
          .defineInternal(ProducerIdExpirationCheckIntervalMsProp, INT, Defaults.PRODUCER_ID_EXPIRATION_CHECK_INTERVAL_MS, atLeast(1), LOW, ProducerIdExpirationCheckIntervalMsDoc)

          /** ********* Fetch Configuration **************/
          .define(MaxIncrementalFetchSessionCacheSlots, INT, Defaults.MAX_INCREMENTAL_FETCH_SESSION_CACHE_SLOTS, atLeast(0), MEDIUM, MaxIncrementalFetchSessionCacheSlotsDoc)
          .define(FetchMaxBytes, INT, Defaults.FETCH_MAX_BYTES, atLeast(1024), MEDIUM, FetchMaxBytesDoc)

          /** ********* Request Limit Configuration ***********/
          .define(MaxRequestPartitionSizeLimit, INT, Defaults.MAX_REQUEST_PARTITION_SIZE_LIMIT, atLeast(1), MEDIUM, MaxRequestPartitionSizeLimitDoc)

          /** ********* Kafka Metrics Configuration ***********/
          .define(MetricNumSamplesProp, INT, Defaults.METRIC_NUM_SAMPLES, atLeast(1), LOW, MetricNumSamplesDoc)
          .define(MetricSampleWindowMsProp, LONG, Defaults.METRIC_SAMPLE_WINDOW_MS, atLeast(1), LOW, MetricSampleWindowMsDoc)
          .define(MetricReporterClassesProp, LIST, Defaults.METRIC_REPORTER_CLASSES, LOW, MetricReporterClassesDoc)
          .define(MetricRecordingLevelProp, STRING, Defaults.METRIC_RECORDING_LEVEL, LOW, MetricRecordingLevelDoc)
          .define(AutoIncludeJmxReporterProp, BOOLEAN, Defaults.AUTO_INCLUDE_JMX_REPORTER, LOW, AutoIncludeJmxReporterDoc)

          /** ********* Kafka Yammer Metrics Reporter Configuration for docs ***********/
          .define(KafkaMetricsReporterClassesProp, LIST, Defaults.KAFKA_METRIC_REPORTER_CLASSES, LOW, KafkaMetricsReporterClassesDoc)
          .define(KafkaMetricsPollingIntervalSecondsProp, INT, Defaults.KAFKA_METRICS_POLLING_INTERVAL_SECONDS, atLeast(1), LOW, KafkaMetricsPollingIntervalSecondsDoc)

          /** ********* Kafka Client Telemetry Metrics Configuration ***********/
          .define(ClientTelemetryMaxBytesProp, INT, Defaults.CLIENT_TELEMETRY_MAX_BYTES, atLeast(1), LOW, ClientTelemetryMaxBytesDoc)

          /** ********* Quota configuration ***********/
          .define(NumQuotaSamplesProp, INT, Defaults.NUM_QUOTA_SAMPLES, atLeast(1), LOW, NumQuotaSamplesDoc)
          .define(NumReplicationQuotaSamplesProp, INT, Defaults.NUM_REPLICATION_QUOTA_SAMPLES, atLeast(1), LOW, NumReplicationQuotaSamplesDoc)
          .define(NumAlterLogDirsReplicationQuotaSamplesProp, INT, Defaults.NUM_ALTER_LOG_DIRS_REPLICATION_QUOTA_SAMPLES, atLeast(1), LOW, NumAlterLogDirsReplicationQuotaSamplesDoc)
          .define(NumControllerQuotaSamplesProp, INT, Defaults.NUM_CONTROLLER_QUOTA_SAMPLES, atLeast(1), LOW, NumControllerQuotaSamplesDoc)
          .define(QuotaWindowSizeSecondsProp, INT, Defaults.QUOTA_WINDOW_SIZE_SECONDS, atLeast(1), LOW, QuotaWindowSizeSecondsDoc)
          .define(ReplicationQuotaWindowSizeSecondsProp, INT, Defaults.REPLICATION_QUOTA_WINDOW_SIZE_SECONDS, atLeast(1), LOW, ReplicationQuotaWindowSizeSecondsDoc)
          .define(AlterLogDirsReplicationQuotaWindowSizeSecondsProp, INT, Defaults.ALTER_LOG_DIRS_REPLICATION_QUOTA_WINDOW_SIZE_SECONDS, atLeast(1), LOW, AlterLogDirsReplicationQuotaWindowSizeSecondsDoc)
          .define(ControllerQuotaWindowSizeSecondsProp, INT, Defaults.CONTROLLER_QUOTA_WINDOW_SIZE_SECONDS, atLeast(1), LOW, ControllerQuotaWindowSizeSecondsDoc)
          .define(ClientQuotaCallbackClassProp, CLASS, null, LOW, ClientQuotaCallbackClassDoc)

          /** ********* General Security Configuration ****************/
          .define(ConnectionsMaxReauthMsProp, LONG, Defaults.CONNECTIONS_MAX_REAUTH_MS, MEDIUM, ConnectionsMaxReauthMsDoc)
          .define(SaslServerMaxReceiveSizeProp, INT, Defaults.SERVER_MAX_RECEIVE_SIZE, MEDIUM, SaslServerMaxReceiveSizeDoc)
          .define(securityProviderClassProp, STRING, null, LOW, securityProviderClassDoc)

          /** ********* SSL Configuration ****************/
          .define(PrincipalBuilderClassProp, CLASS, Defaults.PRINCIPAL_BUILDER, MEDIUM, PrincipalBuilderClassDoc)
          .define(SslProtocolProp, STRING, Defaults.SSL_PROTOCOL, MEDIUM, SslProtocolDoc)
          .define(SslProviderProp, STRING, null, MEDIUM, SslProviderDoc)
          .define(SslEnabledProtocolsProp, LIST, Defaults.SSL_ENABLED_PROTOCOLS, MEDIUM, SslEnabledProtocolsDoc)
          .define(SslKeystoreTypeProp, STRING, Defaults.SSL_KEYSTORE_TYPE, MEDIUM, SslKeystoreTypeDoc)
          .define(SslKeystoreLocationProp, STRING, null, MEDIUM, SslKeystoreLocationDoc)
          .define(SslKeystorePasswordProp, PASSWORD, null, MEDIUM, SslKeystorePasswordDoc)
          .define(SslKeyPasswordProp, PASSWORD, null, MEDIUM, SslKeyPasswordDoc)
          .define(SslKeystoreKeyProp, PASSWORD, null, MEDIUM, SslKeystoreKeyDoc)
          .define(SslKeystoreCertificateChainProp, PASSWORD, null, MEDIUM, SslKeystoreCertificateChainDoc)
          .define(SslTruststoreTypeProp, STRING, Defaults.SSL_TRUSTSTORE_TYPE, MEDIUM, SslTruststoreTypeDoc)
          .define(SslTruststoreLocationProp, STRING, null, MEDIUM, SslTruststoreLocationDoc)
          .define(SslTruststorePasswordProp, PASSWORD, null, MEDIUM, SslTruststorePasswordDoc)
          .define(SslTruststoreCertificatesProp, PASSWORD, null, MEDIUM, SslTruststoreCertificatesDoc)
          .define(SslKeyManagerAlgorithmProp, STRING, Defaults.SSL_KEY_MANAGER_ALGORITHM, MEDIUM, SslKeyManagerAlgorithmDoc)
          .define(SslTrustManagerAlgorithmProp, STRING, Defaults.SSL_TRUST_MANAGER_ALGORITHM, MEDIUM, SslTrustManagerAlgorithmDoc)
          .define(SslEndpointIdentificationAlgorithmProp, STRING, Defaults.SSL_ENDPOINT_IDENTIFICATION_ALGORITHM, LOW, SslEndpointIdentificationAlgorithmDoc)
          .define(SslSecureRandomImplementationProp, STRING, null, LOW, SslSecureRandomImplementationDoc)
          .define(SslClientAuthProp, STRING, Defaults.SSL_CLIENT_AUTHENTICATION, ValidString.in(Defaults.SSL_CLIENT_AUTHENTICATION_VALID_VALUES), MEDIUM, SslClientAuthDoc)
          .define(SslCipherSuitesProp, LIST, Collections.emptyList(), MEDIUM, SslCipherSuitesDoc)
          .define(SslPrincipalMappingRulesProp, STRING, Defaults.SSL_PRINCIPAL_MAPPING_RULES, LOW, SslPrincipalMappingRulesDoc)
          .define(SslEngineFactoryClassProp, CLASS, null, LOW, SslEngineFactoryClassDoc)
          .define(SslAllowDnChangesProp, BOOLEAN, BrokerSecurityConfigs.DEFAULT_SSL_ALLOW_DN_CHANGES_VALUE, LOW, SslAllowDnChangesDoc)
          .define(SslAllowSanChangesProp, BOOLEAN, BrokerSecurityConfigs.DEFAULT_SSL_ALLOW_SAN_CHANGES_VALUE, LOW, SslAllowSanChangesDoc)

          /** ********* Sasl Configuration ****************/
          .define(SaslMechanismInterBrokerProtocolProp, STRING, Defaults.SASL_MECHANISM_INTER_BROKER_PROTOCOL, MEDIUM, SaslMechanismInterBrokerProtocolDoc)
          .define(SaslJaasConfigProp, PASSWORD, null, MEDIUM, SaslJaasConfigDoc)
          .define(SaslEnabledMechanismsProp, LIST, Defaults.SASL_ENABLED_MECHANISMS, MEDIUM, SaslEnabledMechanismsDoc)
          .define(SaslServerCallbackHandlerClassProp, CLASS, null, MEDIUM, SaslServerCallbackHandlerClassDoc)
          .define(SaslClientCallbackHandlerClassProp, CLASS, null, MEDIUM, SaslClientCallbackHandlerClassDoc)
          .define(SaslLoginClassProp, CLASS, null, MEDIUM, SaslLoginClassDoc)
          .define(SaslLoginCallbackHandlerClassProp, CLASS, null, MEDIUM, SaslLoginCallbackHandlerClassDoc)
          .define(SaslKerberosServiceNameProp, STRING, null, MEDIUM, SaslKerberosServiceNameDoc)
          .define(SaslKerberosKinitCmdProp, STRING, Defaults.SASL_KERBEROS_KINIT_CMD, MEDIUM, SaslKerberosKinitCmdDoc)
          .define(SaslKerberosTicketRenewWindowFactorProp, DOUBLE, Defaults.SASL_KERBEROS_TICKET_RENEW_WINDOW_FACTOR, MEDIUM, SaslKerberosTicketRenewWindowFactorDoc)
          .define(SaslKerberosTicketRenewJitterProp, DOUBLE, Defaults.SASL_KERBEROS_TICKET_RENEW_JITTER, MEDIUM, SaslKerberosTicketRenewJitterDoc)
          .define(SaslKerberosMinTimeBeforeReloginProp, LONG, Defaults.SASL_KERBEROS_MIN_TIME_BEFORE_RELOGIN, MEDIUM, SaslKerberosMinTimeBeforeReloginDoc)
          .define(SaslKerberosPrincipalToLocalRulesProp, LIST, Defaults.SASL_KERBEROS_PRINCIPAL_TO_LOCAL_RULES, MEDIUM, SaslKerberosPrincipalToLocalRulesDoc)
          .define(SaslLoginRefreshWindowFactorProp, DOUBLE, Defaults.SASL_LOGIN_REFRESH_WINDOW_FACTOR, MEDIUM, SaslLoginRefreshWindowFactorDoc)
          .define(SaslLoginRefreshWindowJitterProp, DOUBLE, Defaults.SASL_LOGIN_REFRESH_WINDOW_JITTER, MEDIUM, SaslLoginRefreshWindowJitterDoc)
          .define(SaslLoginRefreshMinPeriodSecondsProp, SHORT, Defaults.SASL_LOGIN_REFRESH_MIN_PERIOD_SECONDS, MEDIUM, SaslLoginRefreshMinPeriodSecondsDoc)
          .define(SaslLoginRefreshBufferSecondsProp, SHORT, Defaults.SASL_LOGIN_REFRESH_BUFFER_SECONDS, MEDIUM, SaslLoginRefreshBufferSecondsDoc)
          .define(SaslLoginConnectTimeoutMsProp, INT, null, LOW, SaslLoginConnectTimeoutMsDoc)
          .define(SaslLoginReadTimeoutMsProp, INT, null, LOW, SaslLoginReadTimeoutMsDoc)
          .define(SaslLoginRetryBackoffMaxMsProp, LONG, Defaults.SASL_LOGIN_RETRY_BACKOFF_MAX_MS, LOW, SaslLoginRetryBackoffMaxMsDoc)
          .define(SaslLoginRetryBackoffMsProp, LONG, Defaults.SASL_LOGIN_RETRY_BACKOFF_MS, LOW, SaslLoginRetryBackoffMsDoc)
          .define(SaslOAuthBearerScopeClaimNameProp, STRING, Defaults.SASL_OAUTH_BEARER_SCOPE_CLAIM_NAME, LOW, SaslOAuthBearerScopeClaimNameDoc)
          .define(SaslOAuthBearerSubClaimNameProp, STRING, Defaults.SASL_OAUTH_BEARER_SUB_CLAIM_NAME, LOW, SaslOAuthBearerSubClaimNameDoc)
          .define(SaslOAuthBearerTokenEndpointUrlProp, STRING, null, MEDIUM, SaslOAuthBearerTokenEndpointUrlDoc)
          .define(SaslOAuthBearerJwksEndpointUrlProp, STRING, null, MEDIUM, SaslOAuthBearerJwksEndpointUrlDoc)
          .define(SaslOAuthBearerJwksEndpointRefreshMsProp, LONG, Defaults.SASL_OAUTH_BEARER_JWKS_ENDPOINT_REFRESH_MS, LOW, SaslOAuthBearerJwksEndpointRefreshMsDoc)
          .define(SaslOAuthBearerJwksEndpointRetryBackoffMsProp, LONG, Defaults.SASL_OAUTH_BEARER_JWKS_ENDPOINT_RETRY_BACKOFF_MS, LOW, SaslOAuthBearerJwksEndpointRetryBackoffMsDoc)
          .define(SaslOAuthBearerJwksEndpointRetryBackoffMaxMsProp, LONG, Defaults.SASL_OAUTH_BEARER_JWKS_ENDPOINT_RETRY_BACKOFF_MAX_MS, LOW, SaslOAuthBearerJwksEndpointRetryBackoffMaxMsDoc)
          .define(SaslOAuthBearerClockSkewSecondsProp, INT, Defaults.SASL_OAUTH_BEARER_CLOCK_SKEW_SECONDS, LOW, SaslOAuthBearerClockSkewSecondsDoc)
          .define(SaslOAuthBearerExpectedAudienceProp, LIST, null, LOW, SaslOAuthBearerExpectedAudienceDoc)
          .define(SaslOAuthBearerExpectedIssuerProp, STRING, null, LOW, SaslOAuthBearerExpectedIssuerDoc)

          /** ********* Delegation Token Configuration ****************/
          .define(DelegationTokenSecretKeyAliasProp, PASSWORD, null, MEDIUM, DelegationTokenSecretKeyAliasDoc)
          .define(DelegationTokenSecretKeyProp, PASSWORD, null, MEDIUM, DelegationTokenSecretKeyDoc)
          .define(DelegationTokenMaxLifeTimeProp, LONG, Defaults.DELEGATION_TOKEN_MAX_LIFE_TIME_MS, atLeast(1), MEDIUM, DelegationTokenMaxLifeTimeDoc)
          .define(DelegationTokenExpiryTimeMsProp, LONG, Defaults.DELEGATION_TOKEN_EXPIRY_TIME_MS, atLeast(1), MEDIUM, DelegationTokenExpiryTimeMsDoc)
          .define(DelegationTokenExpiryCheckIntervalMsProp, LONG, Defaults.DELEGATION_TOKEN_EXPIRY_CHECK_INTERVAL_MS, atLeast(1), LOW, DelegationTokenExpiryCheckIntervalDoc)

          /** ********* Password encryption configuration for dynamic configs *********/
          .define(PasswordEncoderSecretProp, PASSWORD, null, MEDIUM, PasswordEncoderSecretDoc)
          .define(PasswordEncoderOldSecretProp, PASSWORD, null, MEDIUM, PasswordEncoderOldSecretDoc)
          .define(PasswordEncoderKeyFactoryAlgorithmProp, STRING, null, LOW, PasswordEncoderKeyFactoryAlgorithmDoc)
          .define(PasswordEncoderCipherAlgorithmProp, STRING, Defaults.PASSWORD_ENCODER_CIPHER_ALGORITHM, LOW, PasswordEncoderCipherAlgorithmDoc)
          .define(PasswordEncoderKeyLengthProp, INT, Defaults.PASSWORD_ENCODER_KEY_LENGTH, atLeast(8), LOW, PasswordEncoderKeyLengthDoc)
          .define(PasswordEncoderIterationsProp, INT, Defaults.PASSWORD_ENCODER_ITERATIONS, atLeast(1024), LOW, PasswordEncoderIterationsDoc)

          /** ********* Raft Quorum Configuration *********/
          .define(RaftConfig.QUORUM_VOTERS_CONFIG, LIST, Defaults.QUORUM_VOTERS, new RaftConfig.ControllerQuorumVotersValidator(), HIGH, RaftConfig.QUORUM_VOTERS_DOC)
          .define(RaftConfig.QUORUM_ELECTION_TIMEOUT_MS_CONFIG, INT, Defaults.QUORUM_ELECTION_TIMEOUT_MS, null, HIGH, RaftConfig.QUORUM_ELECTION_TIMEOUT_MS_DOC)
          .define(RaftConfig.QUORUM_FETCH_TIMEOUT_MS_CONFIG, INT, Defaults.QUORUM_FETCH_TIMEOUT_MS, null, HIGH, RaftConfig.QUORUM_FETCH_TIMEOUT_MS_DOC)
          .define(RaftConfig.QUORUM_ELECTION_BACKOFF_MAX_MS_CONFIG, INT, Defaults.QUORUM_ELECTION_BACKOFF_MS, null, HIGH, RaftConfig.QUORUM_ELECTION_BACKOFF_MAX_MS_DOC)
          .define(RaftConfig.QUORUM_LINGER_MS_CONFIG, INT, Defaults.QUORUM_LINGER_MS, null, MEDIUM, RaftConfig.QUORUM_LINGER_MS_DOC)
          .define(RaftConfig.QUORUM_REQUEST_TIMEOUT_MS_CONFIG, INT, Defaults.QUORUM_REQUEST_TIMEOUT_MS, null, MEDIUM, RaftConfig.QUORUM_REQUEST_TIMEOUT_MS_DOC)
          .define(RaftConfig.QUORUM_RETRY_BACKOFF_MS_CONFIG, INT, Defaults.QUORUM_RETRY_BACKOFF_MS, null, LOW, RaftConfig.QUORUM_RETRY_BACKOFF_MS_DOC)

          /** Internal Configurations **/
          // This indicates whether unreleased APIs should be advertised by this node.
          .defineInternal(UnstableApiVersionsEnableProp, BOOLEAN, false, HIGH)
          // This indicates whether unreleased MetadataVersions should be enabled on this node.
          .defineInternal(UnstableMetadataVersionsEnableProp, BOOLEAN, false, HIGH);

        }
