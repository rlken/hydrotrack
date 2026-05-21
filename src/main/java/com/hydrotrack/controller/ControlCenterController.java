@Controller
public class ControlCenterController {

    @GetMapping("/control-center")
    public String controlCenter() {
        return "control-center";
    }
}
