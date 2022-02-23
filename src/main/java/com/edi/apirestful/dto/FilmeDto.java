package com.edi.apirestful.dto;

import java.util.Objects;

public class FilmeDto {

    private String producer;
    private Long interval;
    private Long previousWin;
    private Long followingWin;

    public FilmeDto(String producer, Long previousWin, Long followingWin) {
        this.producer = producer;
        this.previousWin = previousWin;
        this.followingWin = followingWin;
        this.interval = followingWin - previousWin;
    }

    public String getProducer() {
        return producer;
    }

    public void setProducer(String producer) {
        this.producer = producer;
    }

    public Long getInterval() {
        return interval;
    }

    public void setInterval(Long interval) {
        this.interval = interval;
    }

    public Long getPreviousWin() {
        return previousWin;
    }

    public void setPreviousWin(Long previousWin) {
        this.previousWin = previousWin;
    }

    public Long getFollowingWin() {
        return followingWin;
    }

    public void setFollowingWin(Long followingWin) {
        this.followingWin = followingWin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FilmeDto filmeDto = (FilmeDto) o;
        return Objects.equals(producer, filmeDto.producer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(producer);
    }
}
