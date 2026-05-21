import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ControlCenterController {

    @GetMapping("/control-center")
    public String controlCenter() {
        return "control-center";
    }
}
