package task;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.tool.schema.extract.spi.InformationExtractor;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Data;


@Data
@Entity(name = "tb_tasks")
public class TaskModel {
    private UUID Id;
    private String description;

    @Column(length = 50)
    private String title;
    private LocalDateTime StardAt;
    private LocalDateTime enddAt;
    private String priority;

    private LocalDateTime createdAt;

    @CreationTimestamp
    private UUID idUser;

    public void setTitle(String title) throws Exception{

        if(title.length()>50){
            throw new Exception("O campo não pode ter mais do que 50 caracteres ");
        }
        this.title = title;
    }
}
