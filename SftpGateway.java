
import java.util.List;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.sftp.session.SftpFileInfo;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;

@MessagingGateway
public interface SftpGateway {
	@Gateway(requestChannel = "listSftpChannel")
	public List<SftpFileInfo> listFiles(String dir);

	@Gateway(requestChannel = "toSftpChannel")
	public void sendToSftp(@Payload String file, @Header("file-name") String fileName,
			@Header("remote-target-dir") String remoteDir);
}
