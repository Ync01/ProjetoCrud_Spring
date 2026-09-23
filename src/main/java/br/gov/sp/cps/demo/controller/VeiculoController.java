package br.gov.sp.cps.demo.controller;

import br.gov.sp.cps.demo.model.VeiculoDTO;
import br.gov.sp.cps.demo.service.VeiculoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class VeiculoController {

    @Autowired
    private VeiculoService veiculoService;

    @GetMapping("/CadrastoVeiculo")
    public String cadrastoVeiculo(Model model) {
        model.addAttribute("veiculos", veiculoService.listarVeiculos());
        model.addAttribute("veiculo", new VeiculoDTO());
        return "CadrastoVeiculo";
    }

    @PostMapping("/veiculos")
    public String criar(@ModelAttribute VeiculoDTO veiculoDTO) {
        veiculoService.cadrastoVeiculo(veiculoDTO);
        return "redirect:/CadrastoVeiculo";
    }

    @GetMapping("/veiculos/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        model.addAttribute("veiculos", veiculoService.listarVeiculos());
        model.addAttribute("veiculo", veiculoService.buscarPorId(id));
        return "CadrastoVeiculo";
    }

    @PostMapping("/veiculos/atualizar/{id}")
    public String atualizar(@PathVariable Long id, @ModelAttribute VeiculoDTO veiculoDTO) {
        veiculoService.atualizar(id, veiculoDTO);
        return "redirect:/CadrastoVeiculo";
    }

    @GetMapping("/veiculos/deletar/{id}")
    public String deletar(@PathVariable Long id) {
        veiculoService.deletar(id);
        return "redirect:/CadrastoVeiculo";
    }

    @GetMapping("/veiculos/saida/{id}")
    public String darSaida(@PathVariable Long id) {
        veiculoService.darSaida(id);
        return "redirect:/CadrastoVeiculo";
    }
}