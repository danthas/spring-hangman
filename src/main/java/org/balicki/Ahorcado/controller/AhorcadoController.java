package org.balicki.Ahorcado.controller;

import org.balicki.Ahorcado.model.Palabra;
import org.balicki.Ahorcado.model.UsuarioLoginDTO;
import org.balicki.Ahorcado.model.Utilidad;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.IOException;
import java.net.InetAddress;

@Controller
@RequestMapping("juego")
public class AhorcadoController {
    private boolean borrado = false;
    private boolean victoria = false;
    private int contador;
    private Cookie partidas;
    private Cookie id;

    @GetMapping("ahorcado")
    public ModelAndView devuelveAhorcado(@CookieValue(value = "_id", required = false) String _id,
                                         HttpServletRequest solicitudHttp,
                                         HttpServletResponse respuestaHttp) throws IOException {
        ModelAndView mAV = new ModelAndView();
        if (solicitudHttp.getSession().getAttribute("usuarioLoginDTO") == null) {
            mAV.setViewName("redirect:/acceso/login");
            return mAV;
        }
        String ip = InetAddress.getLocalHost().getHostAddress();
        String mac = Utilidad.direccionMAC();
        String navegador = solicitudHttp.getHeader("user-agent");
        Palabra palabra;
        UsuarioLoginDTO usuarioLoginDTO;
        usuarioLoginDTO = (UsuarioLoginDTO) solicitudHttp.getSession().getAttribute("usuarioLoginDTO");
        if (solicitudHttp.getSession().getAttribute("palabra") != null) {
            palabra = (Palabra) solicitudHttp.getSession().getAttribute("palabra");
        } else {
            palabra = new Palabra();
            solicitudHttp.getSession().setAttribute("palabra", palabra);
        }
        if (victoria) {
            solicitudHttp.getSession().removeAttribute("palabra");
            palabra = new Palabra();
            solicitudHttp.getSession().setAttribute("palabra", palabra);
        }
        if (palabra.getPalabra().equals(String.valueOf(palabra.getArrayPalabra()))) {
            mAV.addObject("resultado", "Has ganado la partida");
            victoria = true;
        } else {
            mAV.addObject("resultado", "Estas jugando la partida");
        }
        if (palabra.getFallos() == 0) {
            mAV.addObject("resultado", "Has perdido la partida");
        } else if (palabra.getFallos() < 0) {
            solicitudHttp.getSession().removeAttribute("palabra");
            palabra = new Palabra();
            solicitudHttp.getSession().setAttribute("palabra", palabra);
            borrado = true;
        }
        if (_id != null) {
            id = new Cookie("_id", solicitudHttp.getCookies()[0].getValue());
            contador = Integer.parseInt(solicitudHttp.getCookies()[1].getValue());
            if (borrado) {
                contador++;
            }
            partidas = new Cookie("_contador", String.valueOf(contador));
            respuestaHttp.addCookie(id);
            respuestaHttp.addCookie(partidas);
            mAV.addObject("bienvenida", "Bienvenido de nuevo");
            borrado = false;
        } else {
            contador = 1;
            id = new Cookie("_id", usuarioLoginDTO.getUsuario());
            partidas = new Cookie("_contador", String.valueOf(contador));
            respuestaHttp.addCookie(id);
            respuestaHttp.addCookie(partidas);
            mAV.addObject("bienvenida", "Bienvenido por primera vez");
        }
        mAV.addObject("textoID", id.getValue());
        mAV.addObject("textoCONT", partidas.getValue());
        mAV.addObject("direccionIP", ip);
        mAV.addObject("direccionMAC", mac);
        mAV.addObject("navegador", navegador);
        mAV.addObject("palabra", palabra.getArrayPalabra());
        mAV.addObject("palabraOculta", palabra.getPalabra());
        mAV.addObject("intentos", palabra.getIntentos());
        mAV.addObject("fallos", palabra.getFallos());
        mAV.addObject("listaIntento", palabra.getListaFallos());
        mAV.setViewName("ahorcado");
        return mAV;
    }

    @PostMapping("ahorcado")
    public ModelAndView procesaAhorcado(HttpServletRequest solicitudHttp, @RequestParam(required = false) String letra) {
        ModelAndView mAV = new ModelAndView();
        if (letra.isBlank()) {
            mAV.setViewName("redirect:ahorcado");
        } else {
            Palabra palabra;
            palabra = (Palabra) solicitudHttp.getSession().getAttribute("palabra");
            palabra.compruebaPalabra(letra);
            solicitudHttp.getSession().setAttribute("palabra", palabra);
            mAV.setViewName("redirect:ahorcado");
        }
        return mAV;
    }

    @PostMapping("nuevaPartida")
    public ModelAndView partidaNueva(HttpServletRequest solicitudHttp) throws IOException {
        ModelAndView mAV = new ModelAndView();
        solicitudHttp.getSession().removeAttribute("palabra");
        Palabra palabra = new Palabra();
        solicitudHttp.getSession().setAttribute("palabra", palabra);
        mAV.setViewName("redirect:ahorcado");
        borrado = true;
        return mAV;
    }
}
