package Model;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

public class Currencies {
    public int id;
    public String code;
    public String fullName;
    public String sign;

    public Currencies(String code, String fullName, String sign){
        this.code = code;
        this.fullName = fullName;
        this.sign = sign;
    }

    public Currencies(){}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    @Override
    public String toString() {
        return "Currencies{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", fullName='" + fullName + '\'' +
                ", sign='" + sign + '\'' +
                '}';
    }
}
