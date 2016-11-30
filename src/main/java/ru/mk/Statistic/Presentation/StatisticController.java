package ru.mk.Statistic.Presentation;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.mk.Statistic.Domain.Hit;
import ru.mk.Statistic.Domain.HitRepository;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;

@RestController
public class StatisticController {

    private final HitRepository hitsRepository;

    public StatisticController() {
        this.hitsRepository = new HitRepository();
    }

    @RequestMapping(
            path={"/hit"},
            method={RequestMethod.POST},
            headers="content-type=application/json"
    )
    @ResponseBody
    public ResponseEntity addHit(@RequestBody String payload, HttpServletRequest request) {
        System.out.println(request.getMethod());
        System.out.println(payload);
        return new ResponseEntity(HttpStatus.BAD_REQUEST);
        /*
        short counterId = Short.valueOf(request.getParameter("counterId"));
        int duration = Integer.valueOf(request.getParameter("duration"));

        System.out.println(request.getParameter("counterId"));
        System.out.println(request.getParameter("duration"));

        if (counterId < 1 || duration < 0) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        this.hitsRepository.persist(new Hit(counterId, duration));
        return new ResponseEntity(HttpStatus.NO_CONTENT);*/
    }

    @RequestMapping(path={"/hit"}, method={RequestMethod.GET})
    @ResponseBody
    public ArrayList<Hit> helloWorldAction(@RequestParam(value="counterId", defaultValue="-1") short counterId) {
        return this.hitsRepository.findByCounterId(counterId);
    }

}
