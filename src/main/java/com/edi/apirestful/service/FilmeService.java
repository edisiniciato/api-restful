package com.edi.apirestful.service;

import com.edi.apirestful.dto.FilmeDto;
import com.edi.apirestful.model.Filme;
import com.edi.apirestful.model.FilmeVencedor;
import com.edi.apirestful.repository.FilmeRepository;
import com.edi.apirestful.view.FilmeVO;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.util.*;

@Service
@Transactional
public class FilmeService implements Serializable {

    private static final String CSV_PATH = "src/main/resources/files/movielist.csv";
    @Autowired
    private FilmeRepository repository;

    public Filme save(Filme filme) {
        return repository.save(filme);
    }

    public Filme update(Filme filme, Long id) {
        Filme find = findById(id);

        find.setYear(filme.getYear());
        find.setTitle(filme.getTitle());
        find.setStudios(filme.getStudios());
        find.setProducers(filme.getProducers());
        return repository.save(find);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public void deleteAll() {
        repository.deleteAll();
    }

    public Filme findById(Long id) {
        return repository.findById(id).orElseThrow(() -> new RuntimeException("Filme não encontrado para o id " + id + "."));
    }

    public void insertToCsv(FilmeService service) {
        service.deleteAll();
        readCsv(service);
    }

    private void readCsv(FilmeService service) {
        try {
            CSVReader csvReader = new CSVReader(new FileReader(CSV_PATH));
            List<List<String>> lines = new ArrayList<List<String>>();
            String[] cols;

            csvReader.readNext();
            while ((cols = csvReader.readNext()) != null) {
                lines.add(Arrays.asList(cols));
            }

            lines.forEach(col -> col.forEach(c -> {
                String[] vect = c.split(";");
                Long year = Long.parseLong(vect[0]);
                String title = vect[1].trim();
                String studios = vect[2].trim();
                String producers = vect[3].trim();
                Filme filme = new Filme(year, title, studios, producers);

                try {
                    int quantidadeMaxVencedor = 10;
                    int pos = 4;
                    for (int i = 0; i < quantidadeMaxVencedor; i++) {
                        if (vect[pos] != null) {
                            String winner = vect[pos].trim();
                            FilmeVencedor vencedor = new FilmeVencedor();
                            vencedor.setFilme(filme);
                            vencedor.setWinner(winner);
                            filme.getWinners().add(vencedor);
                            pos++;
                        }
                    }
                } catch (ArrayIndexOutOfBoundsException e) {
                    return;
                } finally {
                    service.save(filme);
                }
            }));

        } catch (CsvValidationException | IOException e) {
            e.printStackTrace();
        }
    }

    public FilmeDto exportProducersMaxInterval() {
        List<Filme> filmes = repository.findWinners();
        Map<String, List<FilmeVO>> map = new HashMap<>();

        preencherMap(filmes, map);

        List<FilmeDto> dtos = getProducersMaxInterval(map);
        return dtos.stream().max(Comparator.comparing(FilmeDto::getInterval)).get();
    }

    public List<FilmeDto> exportProducersTwoPrizesNext() {
        List<Filme> filmes = repository.findWinners();
        Map<String, List<Long>> map = new HashMap<>();

        preencherMapProdutoresPorAno(filmes, map);

        List<FilmeDto> dtos = getProducersTwoPrizesNext(map);
        removeSecondProducerTwoPrizes(dtos);
        return dtos;
    }

    private List<FilmeDto> getProducersTwoPrizesNext(Map<String, List<Long>> map) {
        List<FilmeDto> dtos = new ArrayList<>();
        for (Map.Entry<String, List<Long>> entry : map.entrySet()) {
            if (entry.getValue().size() > 1) {
                String producer = entry.getKey();

                Long firtYear = entry.getValue().stream().min(Comparator.comparing(Long::longValue)).get();
                Long nextYear = entry.getValue().stream().skip(1).findAny().get();

                FilmeDto filmeDto = new FilmeDto(producer, firtYear, nextYear);
                dtos.add(filmeDto);
            }
        }
        return dtos;
    }

    private void preencherMapProdutoresPorAno(List<Filme> filmes, Map<String, List<Long>> map) {
        filmes.forEach(f -> {
            String producers = f.getProducers().trim();
            if (!map.containsKey(producers)) {
                map.put(producers, new ArrayList<>());
            }
            List<Long> filmesVo = map.get(producers);
            filmesVo.add(f.getYear());
            map.put(producers, filmesVo);
        });
    }

    private void removeSecondProducerTwoPrizes(List<FilmeDto> dtos) {
        FilmeDto firts = dtos.stream().min(Comparator.comparing(FilmeDto::getInterval)).get();
        Iterator<FilmeDto> iterator = dtos.iterator();
        while (iterator.hasNext()) {
            FilmeDto next = iterator.next();
            if (!firts.equals(next)
                    && !next.getInterval().equals(firts.getInterval())) {
                iterator.remove();
            }
        }
    }

    private List<FilmeDto> getProducersMaxInterval(Map<String, List<FilmeVO>> map) {
        List<FilmeDto> dtos = new ArrayList<>();
        for (Map.Entry<String, List<FilmeVO>> entry : map.entrySet()) {
            if (entry.getValue().size() == 2) {
                String producer = entry.getKey();

                Long max = entry.getValue().stream().max(Comparator.comparing(FilmeVO::getYear)).get().getYear();
                Long min = entry.getValue().stream().min(Comparator.comparing(FilmeVO::getYear)).get().getYear();

                FilmeDto filmeDto = new FilmeDto(producer, min, max);
                dtos.add(filmeDto);
            }
        }
        return dtos;
    }

    private void preencherMap(List<Filme> filmes, Map<String, List<FilmeVO>> map) {
        filmes.forEach(f -> {
            String producers = f.getProducers().trim();
            if (!map.containsKey(producers)) {
                map.put(producers, new ArrayList<>());
            }
            List<FilmeVO> filmesVo = map.get(producers);
            FilmeVO filmeVO = new FilmeVO(f.getYear());
            addWinnerMap(f, filmeVO);
            if (filmeVO.getWinners().size() == 2) {
                filmesVo.add(filmeVO);
                map.put(producers, filmesVo);
            }
        });
    }

    private void addWinnerMap(Filme f, FilmeVO filmeVO) {
        List<FilmeVencedor> winners = repository.findWinnersFilme(f);
        for (FilmeVencedor win : winners) {
            filmeVO.getWinners().add(win.getWinner());
        }
    }
}
