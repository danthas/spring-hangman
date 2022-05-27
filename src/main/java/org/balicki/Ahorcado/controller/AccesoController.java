package org.balicki.Ahorcado.controller;

import org.balicki.Ahorcado.model.UsuarioLoginDTO;
import org.balicki.Ahorcado.model.Utilidad;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.net.InetAddress;
import java.net.UnknownHostException;

@Controller
@RequestMapping("acceso")
public class AccesoController {
    public static boolean crearRoot = true;

    @GetMapping("login")
    public ModelAndView devuelveFormularioLogin(HttpServletRequest solicitudHttp) throws UnknownHostException {
        ModelAndView mAV = new ModelAndView();
        if (crearRoot) {
            Utilidad.creaUsuario();
        }
        crearRoot = false;
        UsuarioLoginDTO usuarioLoginDTO;
        String ip = InetAddress.getLocalHost().getHostAddress();
        String mac = Utilidad.direccionMAC();
        String navegador = solicitudHttp.getHeader("user-agent");
        if (solicitudHttp.getSession().getAttribute("usuarioLoginDTO") != null) {
            usuarioLoginDTO = (UsuarioLoginDTO) solicitudHttp.getSession().getAttribute("usuarioLoginDTO");
        } else {
            usuarioLoginDTO = new UsuarioLoginDTO();
        }
        mAV.addObject("usuarioLoginDTO", usuarioLoginDTO);
        mAV.addObject("direccionIP", ip);
        mAV.addObject("direccionMAC", mac);
        mAV.addObject("navegador", navegador);
        mAV.setViewName("login");
        return mAV;
    }

    @PostMapping("login")
    public ModelAndView recibeCredencialesLogin(@Valid UsuarioLoginDTO usuarioLoginDTO, BindingResult br,
                                                HttpServletRequest solicitudHttp) throws UnknownHostException {
        ModelAndView mAV = new ModelAndView();
        String ip = InetAddress.getLocalHost().getHostAddress();
        String mac = Utilidad.direccionMAC();
        String navegador = solicitudHttp.getHeader("user-agent");
        boolean ahorcado = false;
        if (br.hasErrors()) {
            mAV.addObject("otroError", "No se encuentra ningun usuario con los campos dados");
            mAV.addObject("direccionIP", ip);
            mAV.addObject("direccionMAC", mac);
            mAV.addObject("navegador", navegador);
            mAV.addObject("usuarioLoginDTO", usuarioLoginDTO);
            mAV.setViewName("login");
        } else {
            if (usuarioLoginDTO.getUsuario().equals(Utilidad.uLDTO.getUsuario()) && usuarioLoginDTO.getClave().equals(Utilidad.uLDTO.getClave())) {
                ahorcado = true;
            }
            if (ahorcado) {
                solicitudHttp.getSession().setAttribute("usuarioLoginDTO", usuarioLoginDTO);
                mAV.setViewName("redirect:/juego/ahorcado");
            } else {
                mAV.addObject("otroError", "No se encuentra ningun usuario con los campos dados");
                mAV.addObject("direccionIP", ip);
                mAV.addObject("direccionMAC", mac);
                mAV.addObject("navegador", navegador);
                mAV.addObject("usuarioLoginDTO", usuarioLoginDTO);
                mAV.setViewName("login");
            }
        }
        return mAV;
    }

    @GetMapping("logout")
    public ModelAndView desconexion(HttpServletRequest solicitudHttp) {
        ModelAndView mAV = new ModelAndView();
        solicitudHttp.getSession().invalidate();
        mAV.setViewName("redirect:login");
        return mAV;
    }
}
