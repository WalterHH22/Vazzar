# -*- coding: utf-8 -*-
import os
import docx
from docx.shared import Inches, Pt, RGBColor
from docx.enum.text import WD_ALIGN_PARAGRAPH
from docx.enum.table import WD_TABLE_ALIGNMENT
from docx.oxml import parse_xml
from docx.oxml.ns import nsdecls

def create_friendly_document():
    doc = docx.Document()

    # Márgenes de 1 pulgada
    for section in doc.sections:
        section.top_margin = Inches(1.0)
        section.bottom_margin = Inches(1.0)
        section.left_margin = Inches(1.0)
        section.right_margin = Inches(1.0)
        section.different_first_page_header_footer = True
        
        # Pie de página
        footer = section.footer
        f_p = footer.paragraphs[0]
        f_p.alignment = WD_ALIGN_PARAGRAPH.RIGHT
        f_p.text = "VAZZAR | Guía Explicativa del Proyecto para el Equipo"
        f_p.style.font.name = 'Segoe UI'
        f_p.style.font.size = Pt(8.5)
        f_p.style.font.color.rgb = RGBColor(148, 163, 184)

    # Estilo base
    normal_style = doc.styles['Normal']
    normal_style.font.name = 'Segoe UI'
    normal_style.font.size = Pt(10)
    normal_style.font.color.rgb = RGBColor(51, 65, 85) # Slate 700
    normal_style.paragraph_format.line_spacing = 1.25
    normal_style.paragraph_format.space_after = Pt(6)

    def set_cell_background(cell, fill_hex):
        tcPr = cell._tc.get_or_add_tcPr()
        shd = parse_xml(f'<w:shd {nsdecls("w")} w:fill="{fill_hex}"/>')
        tcPr.append(shd)

    def set_cell_margins(cell, top=120, bottom=120, left=150, right=150):
        tcPr = cell._tc.get_or_add_tcPr()
        tcMar = parse_xml(f'<w:tcMar {nsdecls("w")}><w:top w:w="{top}" w:type="dxa"/><w:bottom w:w="{bottom}" w:type="dxa"/><w:left w:w="{left}" w:type="dxa"/><w:right w:w="{right}" w:type="dxa"/></w:tcMar>')
        tcPr.append(tcMar)

    def add_callout(text, title="TIP PARA LA PRESENTACIÓN", border_hex="4F46E5", bg_hex="EEF2FF"):
        tbl = doc.add_table(rows=1, cols=1)
        tbl.alignment = WD_TABLE_ALIGNMENT.CENTER
        tbl.autofit = False
        cell = tbl.cell(0, 0)
        cell.width = Inches(6.5)
        set_cell_background(cell, bg_hex)
        set_cell_margins(cell, top=140, bottom=140, left=180, right=180)
        
        tcPr = cell._tc.get_or_add_tcPr()
        borders = parse_xml(f'<w:tcBorders {nsdecls("w")}><w:top w:val="none"/><w:left w:val="single" w:sz="36" w:space="0" w:color="{border_hex}"/><w:bottom w:val="none"/><w:right w:val="none"/></w:tcBorders>')
        tcPr.append(borders)
        
        p = cell.paragraphs[0]
        p.paragraph_format.space_after = Pt(2)
        run_t = p.add_run(f"💡 {title}: ")
        run_t.bold = True
        run_t.font.name = 'Segoe UI'
        run_t.font.size = Pt(10)
        run_t.font.color.rgb = RGBColor(79, 70, 229)
        
        run_c = p.add_run(text)
        run_c.font.name = 'Segoe UI'
        run_c.font.size = Pt(9.5)
        run_c.font.color.rgb = RGBColor(30, 41, 59)
        doc.add_paragraph()

    def format_heading(text, level=1):
        p = doc.add_paragraph()
        run = p.add_run(text)
        run.bold = True
        run.font.name = 'Segoe UI'
        
        if level == 1:
            p.paragraph_format.space_before = Pt(18)
            p.paragraph_format.space_after = Pt(6)
            run.font.size = Pt(17)
            run.font.color.rgb = RGBColor(15, 23, 42)
            pBdr = parse_xml(f'<w:pBdr {nsdecls("w")}><w:bottom w:val="single" w:sz="18" w:space="4" w:color="4F46E5"/></w:pBdr>')
            p._p.get_or_add_pPr().append(pBdr)
        elif level == 2:
            p.paragraph_format.space_before = Pt(13)
            p.paragraph_format.space_after = Pt(4)
            run.font.size = Pt(13)
            run.font.color.rgb = RGBColor(79, 70, 229)
        elif level == 3:
            p.paragraph_format.space_before = Pt(9)
            p.paragraph_format.space_after = Pt(2)
            run.font.size = Pt(10.5)
            run.font.color.rgb = RGBColor(30, 41, 59)
        return p

    def add_styled_table(headers, data, col_widths=None):
        table = doc.add_table(rows=len(data) + 1, cols=len(headers))
        table.alignment = WD_TABLE_ALIGNMENT.CENTER
        table.autofit = False
        
        hdr_cells = table.rows[0].cells
        for idx, title in enumerate(headers):
            hdr_cells[idx].text = title
            set_cell_background(hdr_cells[idx], "1E293B")
            set_cell_margins(hdr_cells[idx], top=100, bottom=100, left=120, right=120)
            p = hdr_cells[idx].paragraphs[0]
            for r in p.runs:
                r.bold = True
                r.font.name = 'Segoe UI'
                r.font.size = Pt(9.5)
                r.font.color.rgb = RGBColor(255, 255, 255)
        
        for r_idx, row_data in enumerate(data):
            row_cells = table.rows[r_idx + 1].cells
            bg_color = "FFFFFF" if r_idx % 2 == 0 else "F8FAFC"
            for c_idx, val in enumerate(row_data):
                row_cells[c_idx].text = str(val)
                set_cell_background(row_cells[c_idx], bg_color)
                set_cell_margins(row_cells[c_idx], top=80, bottom=80, left=120, right=120)
                p = row_cells[c_idx].paragraphs[0]
                for r in p.runs:
                    r.font.name = 'Segoe UI'
                    r.font.size = Pt(9)
                    r.font.color.rgb = RGBColor(51, 65, 85)
        
        if col_widths:
            for row in table.rows:
                for c_idx, w in enumerate(col_widths):
                    row.cells[c_idx].width = Inches(w)
                    
        doc.add_paragraph()
        return table

    # =========================================================================
    # PORTADA
    # =========================================================================
    title_p = doc.add_paragraph()
    title_p.paragraph_format.space_before = Pt(70)
    title_p.paragraph_format.space_after = Pt(4)
    run_t1 = title_p.add_run("VAZZAR PLATFORM\n")
    run_t1.bold = True
    run_t1.font.name = 'Segoe UI'
    run_t1.font.size = Pt(30)
    run_t1.font.color.rgb = RGBColor(15, 23, 42)

    run_t2 = title_p.add_run("Guía Práctica del Proyecto (Fácil de Entender)")
    run_t2.bold = True
    run_t2.font.name = 'Segoe UI'
    run_t2.font.size = Pt(16)
    run_t2.font.color.rgb = RGBColor(79, 70, 229)

    desc_p = doc.add_paragraph()
    desc_p.paragraph_format.space_before = Pt(12)
    desc_p.paragraph_format.space_after = Pt(120)
    run_d = desc_p.add_run(
        "Este documento explica de forma clara, directa y sin tecnicismos difíciles "
        "qué es Vazzar, qué hace cada parte del sistema, cómo funciona la tienda y la feria virtual, "
        "y cómo presentar el proyecto en equipo con éxito."
    )
    run_d.font.name = 'Segoe UI'
    run_d.font.size = Pt(11)
    run_d.font.color.rgb = RGBColor(100, 116, 139)

    add_styled_table(
        ["Dato", "Detalle"],
        [
            ["Nombre del Proyecto", "Vazzar (E-Commerce & Feria Virtual)"],
            ["Objetivo", "Tienda online de productos + Evento ferial interactivo con puestos de vendedores"],
            ["¿Para quién es?", "Para todo el equipo de trabajo y evaluadores del proyecto"],
            ["Lenguaje del documento", "Explicación sencilla, funcional y orientada a demostración"]
        ],
        col_widths=[2.2, 4.3]
    )

    doc.add_page_break()

    # =========================================================================
    # 1. ¿QUÉ ES VAZZAR? (EN PALABRAS SENCILLAS)
    # =========================================================================
    format_heading("1. ¿Qué es Vazzar? (En palabras sencillas)", level=1)
    
    p = doc.add_paragraph(
        "Imagina un centro comercial moderno que combina dos mundos en una sola página web:\n\n"
        "1. Una Tienda Online Tradicional: Donde entras, buscas ropa, zapatos, café o tecnología con fotos reales de alta calidad, "
        "los metes al carrito de compras y pagas con tarjeta recibiendo tu factura.\n\n"
        "2. Una Feria Virtual Interactiva: Como una feria de exposiciones o bazar de emprendedores, donde cada comerciante o marca "
        "tiene su propio puesto (Stand) con su nombre, color de toldo a su gusto, descripción y una vitrina con sus productos estrella."
    )

    add_callout(
        "Si te preguntan qué hace diferente a Vazzar de una tienda normal como Amazon, responde: "
        "'Vazzar no solo vende productos, sino que le da a cada comerciante un puesto virtual propio dentro de una feria temática donde pueden exhibir su marca directamente'.",
        title="IDEA CLAVE PARA EXPLICAR"
    )

    # =========================================================================
    # 2. ¿CÓMO ESTÁ FORMADO EL PROYECTO?
    # =========================================================================
    format_heading("2. ¿Cómo está formado el proyecto? (Las 2 partes)", level=1)

    p = doc.add_paragraph(
        "El proyecto está dividido limpiamente en dos partes que se comunican entre sí por internet:"
    )

    parts_data = [
        ["1. La Pantalla (Frontend)", "Hecho con React y Vite", "Es todo lo visual que ve el usuario en el navegador o en su celular: los botones, las fotos de los productos, el carrito de compras, los stands de la feria y los formularios."],
        ["2. El Cerebro (Backend y Base de Datos)", "Hecho con Spring Boot (Java) y PostgreSQL", "Es el motor que procesa las compras, calcula el IVA, guarda los productos, verifica las contraseñas y almacena de forma segura toda la información en la base de datos."]
    ]
    add_styled_table(["Parte del Sistema", "¿Con qué se hizo?", "¿Qué hace en la práctica?"], parts_data, col_widths=[1.8, 1.8, 2.9])

    # =========================================================================
    # 3. LAS 4 GRANDES SECCIONES DEL SISTEMA
    # =========================================================================
    format_heading("3. Las 4 Grandes Secciones del Sistema", level=1)

    format_heading("A. La Tienda Online (Store)", level=2)
    p = doc.add_paragraph(
        "Es la página principal del sistema (la portada). Aquí el visitante puede:\n"
        "• Ver productos organizados en 6 categorías: Calzado y Zapatillas, Ropa y Moda, Café y Gourmet, Accesorios y Joyería, Tecnología e Iluminación.\n"
        "• Filtrar con un solo clic: Al presionar cualquier categoría, los productos cambian de inmediato sin recargar la página.\n"
        "• Buscador inteligente: Puedes escribir 'café', 'reloj', 'chaqueta' y te encuentra los productos al instante.\n"
        "• Fotografías reales: Cada producto tiene una foto profesional en alta definición que coincide exactamente con lo que se vende."
    )

    format_heading("B. La Feria Virtual de Comerciantes (Virtual Fair)", level=2)
    p = doc.add_paragraph(
        "Es el área de bazar interactivo del evento:\n"
        "• Cada comerciante tiene su Stand Oficial con su número (ej. STAND #01, STAND #02).\n"
        "• Toldo personalizado: Cada puesto tiene un color distintivo (azul índigo, morado, verde, etc.) elegido por el vendedor.\n"
        "• Pabellones temáticos: Los puestos están divididos por pabellones (Tecnología, Moda, Gourmet, etc.).\n"
        "• Vitrina de productos: Al hacer clic en un puesto, se abre una ventana elegante con los productos que ese comerciante específico tiene a la venta."
    )

    format_heading("C. Carrito de Compras y Pago con Factura (Checkout & Billing)", level=2)
    p = doc.add_paragraph(
        "El proceso de compra completo:\n"
        "1. Al hacer clic en 'Agregar', el producto entra a un carrito flotante lateral.\n"
        "2. Puedes sumar cantidades (+), restar (-) o eliminar ítems.\n"
        "3. Al presionar 'Proceder al Pago', se abre una ventana segura donde ingresas tu nombre, dirección y datos de tarjeta de crédito (con validaciones reales de formato).\n"
        "4. El sistema calcula automáticamente el subtotal, el IVA (13%) y emite una Factura Comercial oficial con número consecutivo."
    )

    format_heading("D. Panel de Inventario y Almacén (Para Comerciantes y Admins)", level=2)
    p = doc.add_paragraph(
        "Es la zona privada para gestionar el negocio:\n"
        "• Crear Categorías: Cualquier vendedor puede crear una nueva categoría para sus productos.\n"
        "• Protección de Categorías: Una categoría solo puede ser borrada por la persona que la creó o por el administrador (aparece bloqueada para los demás vendedores).\n"
        "• Publicar Productos con Fotos: Puedes subir una foto directamente desde tu computadora o pegar un enlace web.\n"
        "• Eliminar Productos: Cada producto en almacén tiene un botón rojo de 'Eliminar' con confirmación previa. Si lo borras, se quita del inventario y de la tienda online automáticamente."
    )

    doc.add_page_break()

    # =========================================================================
    # 4. ROLES DE USUARIO: ¿QUIÉN PUEDE HACER QUÉ?
    # =========================================================================
    format_heading("4. Roles de Usuario: ¿Quién puede hacer qué?", level=1)

    roles_data = [
        ["Comprador / Visitante (Público)", "• Navegar por la tienda y ver productos.\n• Recorrer los stands de la feria virtual.\n• Agregar cosas al carrito y pagar con tarjeta para recibir su factura."],
        ["Comerciante / Vendedor (Merchant)", "• Todo lo del comprador.\n• Diseñar y personalizar su propio Stand en la feria (nombre, color de toldo, descripción).\n• Añadir y quitar productos de la vitrina de su stand.\n• Dar de alta productos en inventario y crear categorías propias."],
        ["Administrador del Sistema (Admin)", "• Control total de la plataforma.\n• Capacidad de borrar cualquier categoría o producto.\n• Panel especial para 'apagar' o 'encender' módulos del sistema en tiempo real con fines de mantenimiento."]
    ]
    add_styled_table(["Tipo de Usuario", "Permisos y Acciones que Puede Realizar"], roles_data, col_widths=[2.3, 4.2])

    # =========================================================================
    # 5. GUÍA RÁPIDA: CÓMO HACER UNA DEMOSTRACIÓN PERFECTA
    # =========================================================================
    format_heading("5. Guía Rápida: Cómo hacer una demostración del proyecto", level=1)

    p = doc.add_paragraph(
        "Si van a exponer el proyecto ante un profesor, compañeros o jurado, sigan estos 5 pasos para lucirse:"
    )

    demo_steps = [
        ["Paso 1: La Portada", "Abran la tienda. Muestren cómo el diseño se ve oscuro, elegante y ordenado. Den clic en las categorías (Calzado, Café, Tecnología) para demostrar que los productos se filtran al instante."],
        ["Paso 2: La Compra", "Elijan 2 productos, abran el carrito, muestren cómo se suman los precios y denle a 'Comprar'. Ingresen los datos de la tarjeta simulada y muestren la confirmación con el cálculo de IVA y factura."],
        ["Paso 3: La Feria Virtual", "Vayan al menú y den clic en 'Feria Virtual'. Muestren los puestos de los comerciantes con sus toldos de colores y abran un puesto para ver su vitrina."],
        ["Paso 4: El Vendedor", "Entren a 'Mi Stand' para demostrar cómo un comerciante puede cambiar el color de su toldo o agregar un producto nuevo a su vitrina en vivo."],
        ["Paso 5: El Inventario", "Vayan a 'Inventario'. Creen una categoría nueva, publiquen un producto con foto y luego demuestren que también se puede eliminar con el botón rojo de confirmación."]
    ]
    add_styled_table(["Momento de la Demo", "¿Qué deben hacer y mostrar?"], demo_steps, col_widths=[1.8, 4.7])

    # =========================================================================
    # 6. PREGUNTAS TÍPICAS QUE LES PUEDEN HACER (Y CÓMO RESPONDER)
    # =========================================================================
    doc.add_page_break()
    format_heading("6. Preguntas Típicas en Exposiciones (FAQ)", level=1)

    faq_data = [
        [
            "¿Dónde se guardan los datos?", 
            "En una base de datos relacional PostgreSQL 18. Está organizada en esquemas separados (uno para usuarios, otro para inventario, otro para la feria, etc.) para que todo esté limpio y ordenado."
        ],
        [
            "¿Qué pasa si borro un producto en inventario?", 
            "El sistema tiene un mecanismo de eventos automático: en cuanto se borra en inventario, se borra de la tienda online y de la vitrina ferial al mismo tiempo, sin dejar productos huérfanos."
        ],
        [
            "¿Las contraseñas de los usuarios son seguras?", 
            "Sí, se encriptan con un algoritmo llamado BCrypt. Ni siquiera los administradores pueden ver la contraseña real de un usuario en la base de datos."
        ],
        [
            "¿Se puede abrir en un celular?", 
            "Totalmente. El diseño es responsive (se adapta automáticamente al tamaño de la pantalla, ya sea una computadora portátil, tablet o teléfono móvil)."
        ],
        [
            "¿Está el proyecto subido a internet?", 
            "Sí, el código está subido a GitHub en dos repositorios (uno para la parte visual y otro para el servidor) y preparado para funcionar en la nube (Render / Vercel / Cloudflare)."
        ]
    ]
    add_styled_table(["Pregunta Posible", "Respuesta Clara y Segura"], faq_data, col_widths=[2.4, 4.1])

    add_callout(
        "Mantengan la calma y hablen con seguridad: el sistema ya fue probado con 19 pruebas automáticas exitosas y "
        "todas las funciones (tienda, feria, stands, inventario, pagos y fotos) están 100% terminadas y funcionando.",
        title="MENSAJE DE ÉXITO PARA EL EQUIPO"
    )

    # Guardado de archivos
    out_proy = r"C:\Users\harol\Desktop\Proyectos\Vazzar\GUIA_PROYECTO_VAZZAR_FACIL.docx"
    out_desk = r"C:\Users\harol\Desktop\GUIA_PROYECTO_VAZZAR_FACIL.docx"
    doc.save(out_proy)
    doc.save(out_desk)
    print("Guía fácil guardada en:")
    print("1.", out_proy)
    print("2.", out_desk)

if __name__ == "__main__":
    create_friendly_document()
