package com.junling.spring.webmvc.components;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SJLView {

    private File templateFile;

    public SJLView(File templateFile) {
        this.templateFile = templateFile;
    }

    public void render(HttpServletRequest req, HttpServletResponse resp, Map<String, Object> model) {
        StringBuffer sb = new StringBuffer();
        try {
            RandomAccessFile raf = new RandomAccessFile(this.templateFile, "r");
            String line = "";

            while ((line=raf.readLine())!= null) {
                line = new String(line.getBytes("iso-8859-1"), "UTF-8");
                Pattern pattern = Pattern.compile("\\$\\{[^\\}]+\\}", Pattern.CASE_INSENSITIVE);
                Matcher matcher = pattern.matcher(line);
                while (matcher.find()) {
                    String paramName = matcher.group();
                    paramName = paramName.replaceAll("\\$\\{|\\}", "");
                    Object paramValue = model.get(paramName);
                    line = matcher.replaceFirst(paramValue.toString());

                    matcher = pattern.matcher(line);
                }
                sb.append(line);
            }

            resp.setCharacterEncoding("UTF-8");
            resp.getWriter().write(sb.toString());
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
