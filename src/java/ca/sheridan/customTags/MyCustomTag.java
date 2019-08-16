package ca.sheridan.customTags;

import javax.servlet.jsp.tagext.*;
import javax.servlet.jsp.*;
import java.io.*;

public class MyCustomTag extends SimpleTagSupport {

    private String msg;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public void doTag() throws JspException, IOException {

        JspWriter out = getJspContext().getOut();
        try {
            out.println("<h3 align='center' style='color:red;'>" + this.msg + "</h3>");

        } catch (Exception e) {
            out.println("ERROR:" + e);
        }
    }
}
