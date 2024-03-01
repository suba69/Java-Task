package JavaTask.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class StatisticsRequest {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date date;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}