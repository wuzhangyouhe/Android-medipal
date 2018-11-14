package sg.edu.nus.iss.se8.medipal.exceptions;


public class MedipalException extends Exception {

    private String message;
    private Integer code = SUCCESS;
    private Level level = Level.MESSAGE;

    public static final Integer SUCCESS = 10;
    public static final Integer DUPLICATE = 20;
    public static final Integer NOT_FOUND = 30;
    public static final Integer NO_RESPONSE = 40;
    public static final Integer DB_ERROR = 50;
    public static final Integer BAD_INPUT = 60;
    public static final Integer DEPENDENT = 70;
    public static final Integer ERROR = 100;

    public enum Level {MESSAGE, MINOR, MAJOR, SEVERE}


    public MedipalException(String message, Integer code, Level level, Exception e) {
        super(e);
        if (e != null) {
            e.printStackTrace();
        }
        this.message = message;
        this.code = code;
        this.level = level;
    }

    @Override
    public String getMessage() {
        return message + ((super.getMessage() != null && !super.getMessage().trim().equals("")) ? ((message != null && !message.trim().equals("")) ? "\n" : "") + super.getMessage() : "");
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
    }
}
