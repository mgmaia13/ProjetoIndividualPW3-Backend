package com.pw3.professor.controller;

import com.pw3.professor.model.Professor;
import com.pw3.professor.model.Sexo;
import com.pw3.professor.repository.ProfessorRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@CrossOrigin("http://localhost:4200") //aceitando requisicao desse endereco
@RestController
public class ProfessorController {
    @Autowired
    ProfessorRepository professorRepository;

    @GetMapping(value = "api/professores")
    public ResponseEntity<List<Professor>> getProfessores(){
        try{
            List<Professor> professores = professorRepository.findAll();
            return new ResponseEntity<>(professores, HttpStatus.OK);
        } catch(Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PostMapping(value = "api/professores")
    public ResponseEntity<Professor> postProfessor(@RequestParam Map<String, String> newProfessor){  //newProfessor nome do map
        try{
            Professor professor = new Professor();
            professor.setName(newProfessor.get("nome"));
            professor.setEndereco(newProfessor.get("endereco"));
            professor.setSalario(Double.parseDouble(newProfessor.get("salario")));
            professor.setTelefone(newProfessor.get("telefone"));
            professor.setIdade(Integer.parseInt(newProfessor.get("idade")));
            professor.setAreaAtuacao(newProfessor.get("areaAtuacao"));
            professor.setSexo(Sexo.values()[Integer.parseInt(newProfessor.get("sexo"))]);
            Professor professorSalvo = professorRepository.save(professor); //salvando post dos atributos
            return new ResponseEntity<>(professorSalvo, HttpStatus.CREATED); //OK/CREATED 200/201
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PutMapping(value = "api/professores/{id}")
    public ResponseEntity<Professor> putProfessor(@PathVariable(value = "id")long id, @RequestParam Map<String, String> newProfessor){
        Optional<Professor>professor = professorRepository.findById(id);
        if (professor.isPresent()){
            Professor professorEditado = professor.get();
            professorEditado.setEndereco(newProfessor.get("endereco"));
            professorEditado.setSalario(Double.parseDouble(newProfessor.get("salario")));
            professorEditado.setTelefone(newProfessor.get("telefone"));
            professorEditado.setIdade(Integer.parseInt(newProfessor.get("idade")));
            professorEditado.setAreaAtuacao(newProfessor.get("areaAtuacao"));
            try{
                Professor professorPut = professorRepository.save(professorEditado);
                return new ResponseEntity<>(professorPut, HttpStatus.OK);
            } catch (Exception e){
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }
    @DeleteMapping(value = "api/professores/{id}")
    public ResponseEntity<Professor> deleteProfessor(@PathVariable(value = "id")long id){
        Optional<Professor>professor = professorRepository.findById(id);
        if(professor.isPresent()){
            try{
                professorRepository.delete(professor.get());
                return new ResponseEntity<>(HttpStatus.OK);
            } catch (Exception e){
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }
}

