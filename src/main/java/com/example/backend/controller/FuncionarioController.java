package com.example.backend.controller;


import com.example.backend.model.Funcionario;
import com.example.backend.repository.FuncionarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@CrossOrigin("http://localhost:4200")
@RestController
public class FuncionarioController {
    @Autowired
    FuncionarioRepository funcionarioRepository;
    //get todos funcionarios
    @GetMapping(value = "api/funcionario")
    public ResponseEntity<List<Funcionario>> getFuncionarios(){
        try{
            List<Funcionario> funcionarios = funcionarioRepository.findAll();
            return new ResponseEntity<>(funcionarios, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //Pegar por ID
    @GetMapping(value = "api/funcionario/{id}")
    public ResponseEntity<Funcionario> getFuncionarioPorId(@PathVariable(value = "id") Long id){
        try{
            Optional<Funcionario> funcionario = funcionarioRepository.findById(id);
            return funcionario.map(value -> new ResponseEntity<>(value, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY));
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //Adicionar funcionario
    @PostMapping("api/funcionario")
    public ResponseEntity<Funcionario> addFuncionario(@RequestParam Map<String, String> novoFuncionario){
        if(novoFuncionario.containsKey("nome")
                && novoFuncionario.containsKey("cpf")
                && novoFuncionario.containsKey("email")
                && novoFuncionario.containsKey("senha")
        ){
            try{
                Funcionario funcionario = new Funcionario(
                        novoFuncionario.get("nome"),
                        novoFuncionario.get("cpf"),
                        novoFuncionario.get("email"),
                        novoFuncionario.get("senha")

                );
                Funcionario funcionarioSalvo = funcionarioRepository.save(funcionario);
                return new ResponseEntity<>(funcionarioSalvo, HttpStatus.CREATED);
            }catch (Exception e){
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }else{
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    //editar funcionario
    @PutMapping("api/funcionario/{id}")
    public ResponseEntity<Funcionario> atualizaFuncionario(
            @PathVariable(value = "id") Long id,
            @RequestParam Map<String, String> novoFuncionario
    ){
        if(novoFuncionario.containsKey("nome")
                && novoFuncionario.containsKey("cpf")
                && novoFuncionario.containsKey("email")){
            Optional<Funcionario> funcionario = funcionarioRepository.findById(id);
            if(funcionario.isPresent()){
                Funcionario funcionarioEditar = funcionario.get();
                funcionarioEditar.setNome(novoFuncionario.get("nome"));
                funcionarioEditar.setCpf(novoFuncionario.get("cpf"));
                funcionarioEditar.setEmail(novoFuncionario.get("email"));
                try{
                    Funcionario funcionarioEditado = funcionarioRepository.save(funcionarioEditar);
                    return new ResponseEntity<>(funcionarioEditado, HttpStatus.OK);
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


    //deletar funcionario
    @DeleteMapping("api/funcionario/{id}")
    public ResponseEntity<Funcionario> deletaFuncionario(@PathVariable(value = "id") Long id){
        try{
            Optional<Funcionario> funcionario = funcionarioRepository.findById(id);
            if(funcionario.isPresent()){
                Funcionario funcionarioDeletar = funcionario.get();
                funcionarioRepository.delete(funcionarioDeletar);
                return new ResponseEntity<>(funcionarioDeletar, HttpStatus.OK);
            }else{
                return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
            }
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //autenticar usuario
    @PostMapping("api/funcionario/auth")
    public ResponseEntity<Funcionario> login(@RequestParam Map<String, String> funcionarioLogin){
        if(funcionarioLogin.containsKey("login") && funcionarioLogin.containsKey("senha")){
            try{
                Funcionario funcionario = funcionarioRepository.findByLogin(funcionarioLogin.get("login"));
                if(Objects.equals(funcionario.getSenha(), funcionarioLogin.get("senha"))){
                    return new ResponseEntity<>(funcionario, HttpStatus.OK);
                }else{
                    return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
                }
            }catch (Exception e){
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }else{
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

}
