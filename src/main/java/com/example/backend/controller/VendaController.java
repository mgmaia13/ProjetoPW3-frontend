package com.example.backend.controller;

import com.example.backend.model.Doce;
import com.example.backend.model.Funcionario;
import com.example.backend.model.Venda;
import com.example.backend.repository.VendaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin("http://localhost:4200")
@RestController
public class VendaController {
    @Autowired
    VendaRepository vendaRepository;
    //get todas Vendas
    @GetMapping(value = "api/venda")
    public ResponseEntity<List<Venda>> getVendas(){
        try{
            List<Venda> vendas = vendaRepository.findAll();
            return new ResponseEntity<>(vendas, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    //Pegar por ID
    @GetMapping(value = "api/venda/{id}")
    public ResponseEntity<Venda> getVendaPorId(@PathVariable(value = "id") Long id){
        try{
            Optional<Venda> venda = vendaRepository.findById(id);
            return venda.map(value -> new ResponseEntity<>(value, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY));
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //deletar venda
    @DeleteMapping("api/venda/{id}")
    public ResponseEntity<Venda> deletaVenda(@PathVariable(value = "id") Long id){
        try{
            Optional<Venda> venda = vendaRepository.findById(id);
            if(venda.isPresent()){
                Venda vendaDeletar = venda.get();
                vendaRepository.delete(vendaDeletar);
                return new ResponseEntity<>(vendaDeletar, HttpStatus.OK);
            }else{
                return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
            }
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
