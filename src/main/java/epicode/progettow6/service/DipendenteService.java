package epicode.progettow6.service;


import epicode.progettow6.exception.NotFoundException;
import epicode.progettow6.model.Dipendente;
import epicode.progettow6.model.DipendenteRequest;
import epicode.progettow6.repository.DipendenteRepository;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

@Service
public class DipendenteService {
    @Autowired
    private DipendenteRepository dipendenteRepository;
    @Autowired
    private JavaMailSenderImpl javaMailSender;
    public Page<Dipendente> getAllDipend(Pageable pageble) {
        return dipendenteRepository.findAll(pageble);
    }

    public Dipendente getDipendenteId(int id) {
        return dipendenteRepository.findById(id).orElseThrow(() -> new NotFoundException("Dipendente assegnato all' id= " + id + " non trovato"));
    }

    public Dipendente saveDipend(DipendenteRequest dipendenteRequest) throws NotFoundException {
        Dipendente dipendente = new Dipendente(dipendenteRequest.getNome(), dipendenteRequest.getCognome(), dipendenteRequest.getUsername(), dipendenteRequest.getEmail());
         sendMail(dipendente.getEmail());
        return dipendenteRepository.save(dipendente);
    }
    public void sendMail(String email){
        SimpleMailMessage message=new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Registrazione");
        message.setText("Registrazione confermata");
        javaMailSender.send(message);
    }

    public Dipendente refreshDipendente(int id, DipendenteRequest dipendenteRequest) throws NotFoundException {
        Dipendente dipendente = getDipendenteId(id);
        dipendente.setDispositivos(dipendenteRequest.getDispositivo());
        dipendente.setEmail(dipendenteRequest.getEmail());
        dipendente.setNome(dipendenteRequest.getNome());
        dipendente.setCognome(dipendenteRequest.getCognome());
        dipendente.setUsername(dipendenteRequest.getUsername());
        return dipendenteRepository.save(dipendente);
    }

    public Dipendente uploadAvatar(int id, String url) throws NotFoundException {
        Dipendente dipendente = getDipendenteId(id);
        dipendente.setAvatar(url);
        return dipendenteRepository.save(dipendente);

    }
    public void deleteDipendente(int id) throws NotFoundException {
        Dipendente dipendente = getDipendenteId(id);
        dipendenteRepository.delete(dipendente);
    }

}
