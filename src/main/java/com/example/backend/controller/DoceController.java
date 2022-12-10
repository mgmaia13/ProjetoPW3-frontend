package com.example.backend.controller;

import com.example.backend.model.Doce;
import com.example.backend.model.Funcionario;
import com.example.backend.repository.DoceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@CrossOrigin("http://localhost:4200")
@RestController
public class DoceController {
    @Autowired
    DoceRepository doceRepository;
    //get todos doces
    @GetMapping(value = "api/doce")
    public ResponseEntity<List<Doce>> getDoces(){
        try{
            List<Doce> doces = doceRepository.findAll();
            return new ResponseEntity<>(doces, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //Adicionar doce
    @PostMapping("api/doce")
    public ResponseEntity<Doce> addDoce(@RequestParam Map<String, String> novoDoce){
        if(novoDoce.containsKey("nome") && novoDoce.containsKey("funcionario")
                && novoDoce.containsKey("preco")){
            try{
                Funcionario funcionario = new Funcionario();
                funcionario.setId(Long.parseLong(novoDoce.get("funcionario")));
                Doce doce = new Doce(
                        novoDoce.get("nome"),
                        novoDoce.get("descricao"),
                        Float.parseFloat(novoDoce.get("preco")),
                        funcionario
                );
                Doce doceSalvo = doceRepository.save(doce);
                return new ResponseEntity<>(doceSalvo, HttpStatus.CREATED);
            }catch (Exception e){
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }else{
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    //editar doce
    @PutMapping("api/doce/{id}")
    public ResponseEntity<Doce> atualizaDoce(
            @PathVariable(value = "id") Long id,
            @RequestParam Map<String, String> novoDoce
    ){
        if(novoDoce.containsKey("nome")
                && novoDoce.containsKey("descricao")
                && novoDoce.containsKey("preco")){
            Optional<Doce> doce = doceRepository.findById(id);
            if(doce.isPresent()){
                Doce doceEditar = doce.get();
                doceEditar.setNome(novoDoce.get("nome"));
                doceEditar.setDescricao(novoDoce.get("descricao"));
                doceEditar.setPreco(Float.parseFloat(novoDoce.get("preco")));
                try{
                    Doce doceEditado = doceRepository.save(doceEditar);
                    return new ResponseEntity<>(doceEditado, HttpStatus.OK);
                }catch (Exception e){
                    return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
                }
            }else{
                return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
            }
        }else{
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }


    //deletar doce
    @DeleteMapping("api/doce/{id}")
    public ResponseEntity<Doce> deletaDoce(@PathVariable(value = "id") Long id){
        try{
            Optional<Doce> doce = doceRepository.findById(id);
            if(doce.isPresent()){
                Doce doceDeletar = doce.get();
                doceRepository.delete(doceDeletar);
                return new ResponseEntity<>(doceDeletar, HttpStatus.OK);
            }else{
                return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
            }
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
