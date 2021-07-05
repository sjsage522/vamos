package io.wisoft.vamos.repository;

import io.wisoft.vamos.domain.uploadphoto.UploadFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UploadFileRepository extends JpaRepository<UploadFile, Long> {

}
