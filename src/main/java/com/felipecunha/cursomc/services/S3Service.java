package com.felipecunha.cursomc.services;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.felipecunha.cursomc.services.exception.FileException;

@Service
public class S3Service {

	private Logger LOG = LoggerFactory.getLogger(S3Service.class);

	@Autowired
	private AmazonS3 s3client;

	@Value("${s3.bucket}")
	private String bucketName;

	//multipart é utilizado porque é desse tipo que o endpoint recebe
	public URI uploadFile(MultipartFile multipartFile) {
		try {
			String fileName = multipartFile.getOriginalFilename(); //informações
			InputStream is = multipartFile.getInputStream();		//básicas
			String contentType = multipartFile.getContentType();	//para fazer upload
			return uploadFile(is, fileName, contentType); 			
			} catch (IOException e) {
				throw new FileException("Erro de IO. Se isso aconteceu, a merda foi grande: " + e.getMessage());
			} 		
	}
	
	//SOBRECARGA DE MÉTODO
	public URI uploadFile(InputStream is, String fileName, String contentType) {
	try {
		ObjectMetadata meta = new ObjectMetadata();
		meta.setContentType(contentType);
		LOG.info("Iniciando upload");
		s3client.putObject(bucketName, fileName, is, meta);
		LOG.info("Upload finalizado");
		return s3client.getUrl(bucketName, fileName).toURI();
	} catch (URISyntaxException e) {
		throw new FileException("Erro ao converter URL para URI. Se isso aconteceu, a merda foi grande");
		}
	}
}