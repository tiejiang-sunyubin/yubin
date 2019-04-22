import org.fkjava.dto.support.DTOHelper;

public class TestMain {
    public static void main(String[] args){
        DTOHelper dto = new DTOHelper();
        try {
            dto.createDto();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
