package com.edi.apirestful.view;

import java.util.ArrayList;
import java.util.List;

public class FilmeVO {

    private Long year;
    private List<String> winners;

    public FilmeVO(Long year) {
        this.year = year;
        this.winners = new ArrayList<>();
    }

    public Long getYear() {
        return year;
    }

    public void setYear(Long year) {
        this.year = year;
    }

    public List<String> getWinners() {
        return winners;
    }

    public void setWinners(List<String> winners) {
        this.winners = winners;
    }
}
