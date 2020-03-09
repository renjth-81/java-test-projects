import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.file.remote.session.CachingSessionFactory;
import org.springframework.integration.file.remote.session.SessionFactory;
import org.springframework.integration.sftp.gateway.SftpOutboundGateway;
import org.springframework.integration.sftp.outbound.SftpMessageHandler;
import org.springframework.integration.sftp.session.DefaultSftpSessionFactory;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;

import com.jcraft.jsch.ChannelSftp.LsEntry;

@Configuration
public class SftpConfig {

	@Value("${sftp.host.url}")
	private String host;

	@Value("${sftp.host.port}")
	private int port;

	@Value("${sftp.host.user}")
	private String user;

	@Value("${sftp.host.password}")
	private String password;

	@Bean
	Properties configProperties() {
		Properties config = new Properties();
		config.setProperty("PreferredAuthentications", "password");
		return config;
	}

	@Bean
	public SessionFactory<LsEntry> sftpSessionFactory() {
		DefaultSftpSessionFactory factory = new DefaultSftpSessionFactory(true);
		factory.setHost(host);
		factory.setPort(port);
		factory.setUser(user);
		factory.setPassword(password);
		factory.setAllowUnknownKeys(true);
		factory.setSessionConfig(configProperties());
		return new CachingSessionFactory<LsEntry>(factory);
	}

	@Bean
	@ServiceActivator(inputChannel = "moveSftpChannel")
	public MessageHandler fileMoveHandler() {
		SftpOutboundGateway sftpOutboundGateway = new SftpOutboundGateway(sftpSessionFactory(), "mv", "payload.from");
		sftpOutboundGateway.setRenameExpressionString("payload.to");
		sftpOutboundGateway.setOutputChannelName("nullChannel");
		return sftpOutboundGateway;
	}

	@Bean
	public IntegrationFlow sftpOutboundListFlow() {
		return IntegrationFlows.from("listSftpChannel")
				.handle(new SftpOutboundGateway(sftpSessionFactory(), "ls", "payload")).get();
	}

	@Bean
	public IntegrationFlow sftpMoveFileFlow() {
		return IntegrationFlows.from("moveSftpChannel").handle(fileMoveHandler()).get();
	}

	/*
	 * transfer masked file
	 */
	@Bean
	@ServiceActivator(inputChannel = "toSftpChannel")
	public MessageHandler handler() {
		SftpMessageHandler handler = new SftpMessageHandler(sftpSessionFactory());
		handler.setRemoteDirectoryExpressionString("headers['remote-target-dir']");
		handler.setFileNameGenerator((Message<?> message) -> (String) message.getHeaders().get("file-name"));
		return handler;
	}

	/*
	 * get one file
	 */
	@Bean
	@ServiceActivator(inputChannel = "streamInputChannel")
	public MessageHandler fetchOneFileHandler() {
		SftpOutboundGateway sftpOutboundGateway = new SftpOutboundGateway(sftpSessionFactory(), "get", "payload");
		sftpOutboundGateway.setOptions("-stream");
		sftpOutboundGateway.setOutputChannelName("streamResponseChannel");
		return sftpOutboundGateway;
	}

	@Bean("streamInputChannel")
	public DirectChannel getstreamInputChannel() {
		return new DirectChannel();
	}

	@Bean("streamResponseChannel")
	public QueueChannel getStreamResponseChannel() {
		return new QueueChannel(10);
	}
}
