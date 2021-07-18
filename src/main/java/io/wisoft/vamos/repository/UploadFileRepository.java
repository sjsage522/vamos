package io.wisoft.vamos.repository;

import io.wisoft.vamos.domain.uploadphoto.UploadFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UploadFileRepository extends JpaRepository<UploadFile, Long> {

    List<UploadFile> findAllByBoardId(Long boardId);

    @Modifying
    @Query("DELETE FROM UploadFile uf WHERE uf.id IN :ids")
    void deleteWithIds(List<Long> ids);
}
