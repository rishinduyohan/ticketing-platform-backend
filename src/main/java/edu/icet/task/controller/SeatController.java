package edu.icet.task.controller;

import edu.icet.task.model.dto.ErrorDTO;
import edu.icet.task.model.dto.SeatHoldResponseDTO;
import edu.icet.task.service.exceptions.SeatLockedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/seats")
public class SeatController {


}
