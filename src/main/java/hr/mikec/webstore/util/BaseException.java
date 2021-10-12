package hr.mikec.webstore.util;

public class BaseException extends Exception{

    private String message;

    public BaseException(String message) {
        super();
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

}
