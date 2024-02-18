package epicode.progettow6.controller;
import com.cloudinary.Cloudinary;

import epicode.progettow6.exception.BadRequestException;
import epicode.progettow6.model.Dipendente;
import epicode.progettow6.model.DipendenteRequest;
import epicode.progettow6.service.DipendenteService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
@RestController
public class DipendenteController {
    @Autowired
    private DipendenteService dipendenteService;
    @Autowired
    private Cloudinary cloudinary;


    @PostMapping("/dipendenti")
    public Dipendente saveDipendente(@RequestBody @Validated DipendenteRequest dipendenteRequest, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            throw  new BadRequestException(bindingResult.getAllErrors().toString());
        }
        return dipendenteService.saveDipend(dipendenteRequest);
    }
    @GetMapping("/dipendenti")
    public Page<Dipendente> getAll(Pageable pageable){
        return dipendenteService.getAllDipend(pageable);
    }

    @GetMapping("/dipendenti/{id}")
    public Dipendente getDipendenteById(@PathVariable int id){
        return dipendenteService.getDipendenteId(id);
    }
    @PutMapping("/dipendenti/{id}")
    public  Dipendente updateDipendente(@PathVariable int id,@RequestBody @Validated DipendenteRequest dipendenteRequest,BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw  new BadRequestException(bindingResult.getAllErrors().toString());

        }
        return  dipendenteService.refreshDipendente(id,dipendenteRequest);
    }


    @PatchMapping("/dipendenti/{id}/upload")
    public  Dipendente uploadAvatar(@PathVariable int id, @RequestParam("upload")MultipartFile file) throws BadRequestException, IOException {
        return  dipendenteService.uploadAvatar(id,(String)cloudinary.uploader().upload(file.getBytes(),new HashMap()).get("url") );
    }
    @DeleteMapping("/dipendenti/{id}")
    public  void  deleteDipendente(@PathVariable int id){dipendenteService.deleteDipendente(id);}
}
