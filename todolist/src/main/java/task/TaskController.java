package task;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.Utils.Utils;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/tasks")
public class TaskController {
    @Autowired
    private ITaskRepository taskRepository;

    @PostMapping("/")
    public ResponseEntity create(@RequestBody TaskModel model, HttpServletRequest request) {

        var idUser = request.getAttribute("idUser");
        model.setIdUser((UUID) idUser);

        var currentDate = LocalDateTime.now();
        if (currentDate.isAfter(model.getStardAt()) || currentDate.isAfter(model.getEnddAt())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("A data de início / dara de término deve ser maior do que a data atual");
        }

        if (model.getStardAt().isAfter(model.getEnddAt())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("A data de início dever ser menor do que a data de término");
        }

        var task = this.taskRepository.save(model);
        return ResponseEntity.status(HttpStatus.OK).body(task);
    }

    @GetMapping("/")
    public List<TaskModel> list(HttpServletRequest request) {
        var iduser = request.getAttribute("idUser");
        var tasks = this.taskRepository.findByIdUser((UUID) iduser);
        return tasks;
    }

    @PutMapping("/{id}")

    public ResponseEntity update(TaskModel model, HttpServletRequest request, @PathVariable UUID Id) {
        var task = this.taskRepository.findById(Id).orElse(null);

        var iduser = request.getAttribute("idUser");
        if (task.getIdUser().equals(iduser)) {
              return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Usuario sem permissão");

        }
        Utils.copyNomNullProperties(model, task);
        var taskresult = this.taskRepository.save(task);
        return ResponseEntity.status(HttpStatus.OK).body(this.taskRepository.save(taskresult));
    }
}
