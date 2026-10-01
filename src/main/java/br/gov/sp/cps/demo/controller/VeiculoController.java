package br.gov.sp.cps.demo.controller;

import br.gov.sp.cps.demo.exception.RegraNegocioException;
import br.gov.sp.cps.demo.model.VeiculoDTO;
import br.gov.sp.cps.demo.service.VeiculoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

// GET /veiculos é liberado para ADMIN e USER. Todas as outras rotas daqui
// só chegam ao controller se o usuário for ADMIN (ver AdminInterceptor).
@Controller
public class VeiculoController {

    private final VeiculoService veiculoService;

    public VeiculoController(VeiculoService veiculoService) {
        this.veiculoService = veiculoService;
    }

    // Lista, pesquisa (?placa=) e histórico
    @GetMapping("/veiculos")
    public String listar(@RequestParam(required = false) String placa, Model model) {
        if (!model.containsAttribute("veiculo")) {
            model.addAttribute("veiculo", new VeiculoDTO());
        }
        carregarListas(model, placa);
        return "cadastroVeiculo";
    }

    @GetMapping("/veiculos/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        model.addAttribute("veiculo", veiculoService.buscarPorId(id));
        carregarListas(model, null);
        return "cadastroVeiculo";
    }

    @PostMapping("/veiculos")
    public String criar(@ModelAttribute VeiculoDTO veiculoDTO, RedirectAttributes redirect) {
        try {
            redirect.addFlashAttribute("sucesso", veiculoService.cadastrarVeiculo(veiculoDTO));
        } catch (RegraNegocioException e) {
            redirect.addFlashAttribute("erro", e.getMessage());
            redirect.addFlashAttribute("veiculo", veiculoDTO);
        }
        return "redirect:/veiculos";
    }

    @PostMapping("/veiculos/atualizar/{id}")
    public String atualizar(@PathVariable Long id, @ModelAttribute VeiculoDTO veiculoDTO, RedirectAttributes redirect) {
        try {
            redirect.addFlashAttribute("sucesso", veiculoService.atualizar(id, veiculoDTO));
            return "redirect:/veiculos";
        } catch (RegraNegocioException e) {
            redirect.addFlashAttribute("erro", e.getMessage());
            return "redirect:/veiculos/editar/" + id;
        }
    }

    @PostMapping("/veiculos/saida/{id}")
    public String darSaida(@PathVariable Long id, RedirectAttributes redirect) {
        try {
            redirect.addFlashAttribute("sucesso", veiculoService.darSaida(id));
        } catch (RegraNegocioException e) {
            redirect.addFlashAttribute("erro", e.getMessage());
        }
        return "redirect:/veiculos";
    }

    @PostMapping("/veiculos/deletar/{id}")
    public String deletar(@PathVariable Long id, RedirectAttributes redirect) {
        redirect.addFlashAttribute("sucesso", veiculoService.deletar(id));
        return "redirect:/veiculos";
    }

    // Endereço antigo do projeto, mantido para não quebrar links salvos
    @GetMapping("/CadrastoVeiculo")
    public String rotaAntiga() {
        return "redirect:/veiculos";
    }

    private void carregarListas(Model model, String placa) {
        String filtro = placa == null ? "" : placa.trim().toUpperCase().replace("-", "");
        model.addAttribute("placa", filtro);
        model.addAttribute("veiculosAtivos", filtrar(veiculoService.listarAtivos(), filtro));
        model.addAttribute("veiculosHistorico", filtrar(veiculoService.listarHistorico(), filtro));
    }

    private List<VeiculoDTO> filtrar(List<VeiculoDTO> veiculos, String filtro) {
        if (filtro.isEmpty()) {
            return veiculos;
        }
        return veiculos.stream().filter(v -> v.getPlaca() != null && v.getPlaca().contains(filtro)).toList();
    }
}
