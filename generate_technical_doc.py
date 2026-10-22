# -*- coding: utf-8 -*-
import os
import docx
from docx.shared import Inches, Pt, RGBColor
from docx.enum.text import WD_ALIGN_PARAGRAPH
from docx.enum.table import WD_TABLE_ALIGNMENT, WD_ALIGN_VERTICAL
from docx.oxml import OxmlElement, parse_xml
from docx.oxml.ns import nsdecls, qn

def create_document():
    doc = docx.Document()

    # Configuración de márgenes (1 pulgada = 2.54 cm)
    for section in doc.sections:
        section.top_margin = Inches(1.0)
        section.bottom_margin = Inches(1.0)
        section.left_margin = Inches(1.0)
        section.right_margin = Inches(1.0)
        section.different_first_page_header_footer = True
        
        # Pie de página con numeración
        footer = section.footer
        f_p = footer.paragraphs[0]
        f_p.alignment = WD_ALIGN_PARAGRAPH.RIGHT
        f_p.text = "VAZZAR Platform | Documento de Especificación Técnica"
        f_p.style.font.name = 'Segoe UI'
        f_p.style.font.size = Pt(8.5)
        f_p.style.font.color.rgb = RGBColor(148, 163, 184)

    # Estilos de fuente globales
    normal_style = doc.styles['Normal']
    normal_style.font.name = 'Segoe UI'
    normal_style.font.size = Pt(10)
    normal_style.font.color.rgb = RGBColor(51, 65, 85) # Slate 700
    normal_style.paragraph_format.line_spacing = 1.2
    normal_style.paragraph_format.space_after = Pt(6)

    # Helpers de estilo y XML
    def set_cell_background(cell, fill_hex):
        tcPr = cell._tc.get_or_add_tcPr()
        shd = parse_xml(f'<w:shd {nsdecls("w")} w:fill="{fill_hex}"/>')
        tcPr.append(shd)

    def set_cell_margins(cell, top=120, bottom=120, left=150, right=150):
        tcPr = cell._tc.get_or_add_tcPr()
        tcMar = parse_xml(f'<w:tcMar {nsdecls("w")}><w:top w:w="{top}" w:type="dxa"/><w:bottom w:w="{bottom}" w:type="dxa"/><w:left w:w="{left}" w:type="dxa"/><w:right w:w="{right}" w:type="dxa"/></w:tcMar>')
        tcPr.append(tcMar)

    def add_callout(text, title="NOTA IMPORTANTE", border_hex="4F46E5", bg_hex="F8FAFC"):
        tbl = doc.add_table(rows=1, cols=1)
        tbl.alignment = WD_TABLE_ALIGNMENT.CENTER
        tbl.autofit = False
        cell = tbl.cell(0, 0)
        cell.width = Inches(6.5)
        set_cell_background(cell, bg_hex)
        set_cell_margins(cell, top=140, bottom=140, left=200, right=200)
        
        tcPr = cell._tc.get_or_add_tcPr()
        borders = parse_xml(f'<w:tcBorders {nsdecls("w")}><w:top w:val="none"/><w:left w:val="single" w:sz="36" w:space="0" w:color="{border_hex}"/><w:bottom w:val="none"/><w:right w:val="none"/></w:tcBorders>')
        tcPr.append(borders)
        
        p = cell.paragraphs[0]
        p.paragraph_format.space_after = Pt(2)
        run_t = p.add_run(f"📌 {title}: ")
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
            run.font.size = Pt(18)
            run.font.color.rgb = RGBColor(15, 23, 42) # Slate 900
            # Línea decorativa inferior
            pBdr = parse_xml(f'<w:pBdr {nsdecls("w")}><w:bottom w:val="single" w:sz="18" w:space="4" w:color="4F46E5"/></w:pBdr>')
            p._p.get_or_add_pPr().append(pBdr)
        elif level == 2:
            p.paragraph_format.space_before = Pt(14)
            p.paragraph_format.space_after = Pt(4)
            run.font.size = Pt(13.5)
            run.font.color.rgb = RGBColor(79, 70, 229) # Indigo 600
        elif level == 3:
            p.paragraph_format.space_before = Pt(10)
            p.paragraph_format.space_after = Pt(2)
            run.font.size = Pt(11)
            run.font.color.rgb = RGBColor(30, 41, 59)
        return p

    def add_styled_table(headers, data, col_widths=None):
        table = doc.add_table(rows=len(data) + 1, cols=len(headers))
        table.alignment = WD_TABLE_ALIGNMENT.CENTER
        table.autofit = False
        
        # Estilo encabezado
        hdr_cells = table.rows[0].cells
        for idx, title in enumerate(headers):
            hdr_cells[idx].text = title
            set_cell_background(hdr_cells[idx], "1E293B") # Slate 800
            set_cell_margins(hdr_cells[idx], top=100, bottom=100, left=120, right=120)
            p = hdr_cells[idx].paragraphs[0]
            p.alignment = WD_ALIGN_PARAGRAPH.LEFT
            for r in p.runs:
                r.bold = True
                r.font.name = 'Segoe UI'
                r.font.size = Pt(9.5)
                r.font.color.rgb = RGBColor(255, 255, 255)
        
        # Filas de datos
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
        
        # Anchos de columna si existen
        if col_widths:
            for row in table.rows:
                for c_idx, w in enumerate(col_widths):
                    row.cells[c_idx].width = Inches(w)
                    
        doc.add_paragraph() # Espacio
        return table

    # =========================================================================
    # PORTADA CORPORATIVA
    # =========================================================================
    title_p = doc.add_paragraph()
    title_p.paragraph_format.space_before = Pt(80)
    title_p.paragraph_format.space_after = Pt(4)
    run_t1 = title_p.add_run("VAZZAR PLATFORM\n")
    run_t1.bold = True
    run_t1.font.name = 'Segoe UI'
    run_t1.font.size = Pt(32)
    run_t1.font.color.rgb = RGBColor(15, 23, 42)

    run_t2 = title_p.add_run("Documento de Arquitectura y Especificación Técnica")
    run_t2.bold = True
    run_t2.font.name = 'Segoe UI'
    run_t2.font.size = Pt(16)
    run_t2.font.color.rgb = RGBColor(79, 70, 229)

    desc_p = doc.add_paragraph()
    desc_p.paragraph_format.space_before = Pt(10)
    desc_p.paragraph_format.space_after = Pt(140)
    run_d = desc_p.add_run(
        "Sistema integral de Comercio Electrónico B2C, Feria Virtual Interactiva de Comerciantes, "
        "Inventario Desacoplado y Módulo de Facturación. Diseñado bajo Arquitectura Hexagonal y Domain-Driven Design (DDD)."
    )
    run_d.font.name = 'Segoe UI'
    run_d.font.size = Pt(11)
    run_d.font.color.rgb = RGBColor(100, 116, 139)

    # Tabla de metadatos en portada
    meta_p = doc.add_paragraph()
    meta_p.paragraph_format.space_after = Pt(2)
    r_meta_t = meta_p.add_run("INFORMACIÓN DEL PROYECTO")
    r_meta_t.bold = True
    r_meta_t.font.name = 'Segoe UI'
    r_meta_t.font.size = Pt(10)
    r_meta_t.font.color.rgb = RGBColor(79, 70, 229)

    meta_data = [
        ["Versión del Software", "2.0.0 (Cloud & Production Ready)"],
        ["Fecha de Publicación", "Octubre 2026"],
        ["Backend Repository", "https://github.com/WalterHH22/Vazzar.git"],
        ["Frontend Repository", "https://github.com/Hardcode275/vazzar-frontend.git"],
        ["Stack Tecnológico", "Spring Boot 4 (Java 21), React 19 + Vite, PostgreSQL 18, Flyway, Docker"],
        ["Infraestructura de Nube", "Render (Web Service + Managed PostgreSQL) & Vercel / Cloudflare Tunnels"]
    ]
    add_styled_table(["Parámetro", "Detalle Técnico"], meta_data, col_widths=[2.2, 4.3])

    doc.add_page_break()

    # =========================================================================
    # 1. RESUMEN EJECUTIVO Y OBJETIVOS
    # =========================================================================
    format_heading("1. Resumen Ejecutivo y Objetivos del Sistema", level=1)
    
    p = doc.add_paragraph(
        "VAZZAR es una plataforma web moderna diseñada para la convergencia entre el comercio minorista digital (E-Commerce) "
        "y eventos comerciales masivos interactivos (Feria Virtual). La solución permite a comerciantes independientes, marcas "
        "y visitantes converger en un mismo ecosistema unificado de compra, exhibición de productos, pagos en línea y administración de inventario."
    )
    
    format_heading("1.1 Objetivos de Negocio y Técnicos", level=2)
    p = doc.add_paragraph(
        "• Experiencia de Usuario Unificada: Catálogo público elegante con fotografía en alta resolución y navegación por categorías limpias.\n"
        "• Feria Virtual de Comerciantes: Espacio interactivo donde cada vendedor oficial posee un Stand temático con toldo personalizable, datos de contacto y vitrina de productos.\n"
        "• Arquitectura Hexagonal y Modular: Aislamiento total de lógica entre Bounded Contexts (Identity, Inventory, Ecommerce, Fair, Billing).\n"
        "• Event-Driven Synchronization: Sincronización automática mediante eventos de dominio en memoria, evitando acoplamiento de bases de datos.\n"
        "• Alta Disponibilidad y Portabilidad: Compatible con Docker, Render, Railway, Vercel y entornos locales mediante migraciones automatizadas de Flyway."
    )

    add_callout(
        "El sistema fue construido con una política de cero acoplamiento entre esquemas de base de datos. Ningún esquema externo "
        "realiza llaves foráneas cruzadas hacia otros esquemas de negocio, garantizando la posibilidad de extraer cada módulo a un microservicio independiente en el futuro.",
        title="PRINCIPIO ARQUITECTÓNICO FUNDAMENTAL"
    )

    # =========================================================================
    # 2. ARQUITECTURA DE SOFTWARE (HEXAGONAL & DDD)
    # =========================================================================
    format_heading("2. Arquitectura de Software (Hexagonal / Ports & Adapters)", level=1)
    
    p = doc.add_paragraph(
        "El Backend implementa estrictamente los principios de Ports and Adapters (Arquitectura Hexagonal). "
        "Cada módulo funcional está delimitado como un Bounded Context autónomo:"
    )

    modules_data = [
        ["Identity", "Gestión de usuarios, credenciales, roles (Admin / Merchant / User), generación y validación de tokens JWT (HS256) con contraseñas encriptadas mediante BCrypt."],
        ["Inventory", "Núcleo de productos físicos, control de existencias (stock), gestión de proveedores y categorías jerárquicas con trazabilidad de usuario creador."],
        ["Ecommerce", "Catálogo de cara al comprador final, sincronización de precios e imágenes de alta definición, filtrado por categorías y control de visibilidad."],
        ["Fair", "Feria virtual interactiva, asignación de Stands oficiales a comerciantes, personalización de pabellones, colores de toldo y vitrinas comerciales exclusivas."],
        ["Billing", "Procesamiento de checkout con tarjeta de crédito, validación algorítmica, desglose impositivo de IVA (13%), cálculo de totales y emisión de facturas consecutivas."],
        ["Shared", "Control dinámico de módulos en tiempo real (ModuleGuardInterceptor), configuración de seguridad web (CORS) y servicios de carga de archivos (Multipart/Upload)."]
    ]
    add_styled_table(["Bounded Context", "Responsabilidad de Dominio y Negocio"], modules_data, col_widths=[1.6, 4.9])

    format_heading("2.1 Patrón de Eventos de Dominio (Event-Driven Synchronization)", level=2)
    p = doc.add_paragraph(
        "La comunicación inter-modular no se realiza mediante inyecciones directas de repositorios ajenos, sino mediante eventos publicados por Spring ApplicationEventPublisher:"
    )

    events_data = [
        ["ProductCreatedEvent", "Inventory (Publisher)", "Ecommerce & Fair (Listeners)", "Al registrar un producto en almacén, se replica en el catálogo online y, si es elegible, en la feria."],
        ["ProductDeletedEvent", "Inventory (Publisher)", "Ecommerce & Fair (Listeners)", "Al eliminar un producto físico, se retira de la tienda virtual y se desvincula de las vitrinas de stands feriales."],
        ["CategoryCreatedEvent", "Inventory (Publisher)", "Ecommerce (Listener)", "Nuevas categorías creadas por vendedores se crean automáticamente en el menú del e-commerce."],
        ["CategoryDeletedEvent", "Inventory (Publisher)", "Ecommerce (Listener)", "La remoción de una categoría en inventario elimina la categoría sincronizada en el storefront público."]
    ]
    add_styled_table(["Evento de Dominio", "Emisor", "Receptores", "Impacto y Comportamiento en el Negocio"], events_data, col_widths=[1.8, 1.4, 1.5, 1.8])

    # =========================================================================
    # 3. MODELO DE BASE DE DATOS Y ESQUEMAS POSTGRESQL
    # =========================================================================
    doc.add_page_break()
    format_heading("3. Modelo de Base de Datos y Esquemas PostgreSQL", level=1)
    
    p = doc.add_paragraph(
        "La base de datos relacional PostgreSQL 18 está organizada mediante esquemas lógicos dedicados, asegurando que cada módulo "
        "tenga propiedad exclusiva sobre sus tablas:"
    )

    db_schemas_data = [
        ["identity", "user", "id (UUID PK), email (UQ), password_hash (BCrypt), role, active, created_at, updated_at"],
        ["inventory", "company", "id (UUID PK), user_id, name, tax_id, fiscal_address, phone, created_at"],
        ["inventory", "category", "id (UUID PK), name (UQ), created_by (UUID), created_by_email (VARCHAR)"],
        ["inventory", "product", "id (UUID PK), company_id (FK), name, description, stock, fair_eligible, created_at, updated_at"],
        ["ecommerce", "category", "id (UUID PK), name"],
        ["ecommerce", "product", "id (UUID PK), company_id, category_id (FK ecom.cat), name, description, price, available, image_url"],
        ["fair", "stand", "id (UUID PK), merchant_email (UQ), merchant_name, stand_number, name, pavilion, canopy_color, description, logo_url"],
        ["fair", "stand_product", "id (UUID PK), stand_id (FK fair.stand), product_id, name, description, price, stock, image_url"],
        ["billing", "invoice", "id (UUID PK), invoice_number (UQ), user_id, payment_method, status, total, tax, issued_at"],
        ["billing", "invoice_item", "id (UUID PK), invoice_id (FK), product_id, product_name, quantity, unit_price, subtotal"]
    ]
    add_styled_table(["Esquema", "Tabla", "Campos Clave y Restricciones"], db_schemas_data, col_widths=[1.2, 1.4, 3.9])

    format_heading("3.1 Estrategia de Versionamiento con Flyway", level=2)
    p = doc.add_paragraph(
        "Toda evolución del esquema se gestiona a través de scripts de migración Flyway versionados secuencialmente:"
    )

    migrations_data = [
        ["V1_0", "Creación del esquema identity y tabla de usuarios con soporte de roles."],
        ["V2_0", "Creación del esquema inventory: empresas, categorías, subcategorías y productos físicos."],
        ["V3_0", "Creación del esquema billing: facturas, ítems y clientes."],
        ["V4_0", "Creación del esquema ecommerce: catálogo de cara al público y carritos."],
        ["V5_0", "Creación del esquema fair: ferias y expositores comerciales."],
        ["V6_0 - V9_0", "Evolución de permisos de categorías, trazabilidad de creador (created_by) y auditoría por email."],
        ["V10_0", "Persistencia de Stands feriales completos por comerciante y vitrina de productos."],
        ["V11_0", "Semilla de catálogo enriquecido en 6 categorías maestras y corrección de llaves foráneas iniciales."],
        ["V12_0", "Curaduría visual de imágenes Unsplash de alta resolución vinculadas a descripciones exactas."]
    ]
    add_styled_table(["Versión Flyway", "Objetivo y Cambios Estructurales"], migrations_data, col_widths=[1.5, 5.0])

    # =========================================================================
    # 4. ESPECIFICACIÓN DE ENDPOINTS API REST
    # =========================================================================
    doc.add_page_break()
    format_heading("4. Especificación de Endpoints API REST", level=1)
    
    p = doc.add_paragraph(
        "La interfaz REST proporciona comunicación síncrona mediante JSON bajo especificaciones HTTP estándar:"
    )

    endpoints_data = [
        ["POST", "/api/identity/login", "Público", "Autenticación de credenciales, emite JWT Bearer Token."],
        ["POST", "/api/identity/register", "Público", "Registro de nuevo usuario con rol asignado (User / Merchant)."],
        ["GET", "/api/ecommerce/products", "Público", "Lista todos los productos activos de la tienda con categoría y fotos."],
        ["GET", "/api/ecommerce/categories", "Público", "Obtiene las categorías disponibles en el storefront."],
        ["GET", "/api/fair/stands", "Público", "Lista todos los puestos comerciales oficiales de la feria virtual."],
        ["GET", "/api/fair/stands/my-stand?email=", "Merchant", "Consulta el stand propio asignado al correo del comerciante."],
        ["POST", "/api/fair/stands", "Merchant", "Inaugura un nuevo stand comercial si no existía previamente."],
        ["PUT", "/api/fair/stands/{id}", "Merchant", "Actualiza colores de toldo, pabellón, logotipo y descripción del stand."],
        ["POST", "/api/fair/stands/{id}/products", "Merchant", "Añade un producto específico a la vitrina del puesto."],
        ["DELETE", "/api/fair/stands/{id}/products/{pId}", "Merchant", "Retira un producto de la vitrina ferial."],
        ["GET", "/api/inventory/categories", "Público/Auth", "Retorna categorías con indicador de creador y permisos de borrado."],
        ["POST", "/api/inventory/categories", "Auth", "Crea nueva categoría física (replicada inmediatamente a ecommerce)."],
        ["DELETE", "/api/inventory/categories/{id}", "Owner/Admin", "Elimina categoría respetando ownership (solo creador o admin)."],
        ["POST", "/api/inventory/products", "Auth", "Crea producto físico con imagen y precio (publicado a la tienda)."],
        ["DELETE", "/api/inventory/products/{id}", "Auth", "Elimina producto físico y desincroniza tienda y feria en cascada."],
        ["POST", "/api/upload", "Auth/Public", "Sube imagen binaria (JPG/PNG/WEBP) al servidor de archivos estáticos."],
        ["POST", "/api/billing/checkout", "Público/Auth", "Procesa pago con tarjeta de crédito y emite factura fiscal oficial."],
        ["PATCH", "/api/admin/modules/{id}/toggle", "Admin", "Enciende o apaga Bounded Contexts en caliente (Retorna 503 si inactivo)."]
    ]
    add_styled_table(["Método", "Ruta del Endpoint", "Seguridad", "Descripción del Comportamiento"], endpoints_data, col_widths=[0.9, 2.3, 1.0, 2.3])

    # =========================================================================
    # 5. FRONTEND Y EXPERIENCIA DE USUARIO (UI/UX)
    # =========================================================================
    format_heading("5. Frontend y Experiencia de Usuario (UI/UX)", level=1)
    
    p = doc.add_paragraph(
        "La interfaz de usuario está construida sobre React 19 y empaquetada con Vite para máxima velocidad. "
        "Se adoptó un sistema de diseño visual sólido, moderno y elegante:"
    )

    p = doc.add_paragraph(
        "• Paleta Cromática: Fondo principal profundo Slate (#090d16), tarjetas elevadas (#131b2e), bordes de alta definición (#1e293b) y acentos en Índigo Vibrante (#6366f1).\n"
        "• Tienda Online (StorePage): Carrusel de píldoras filtrables por categoría, buscador en vivo por texto en nombre o descripción, insignia flotante de categoría, precio contrastado y botón interactivo de carrito.\n"
        "• Carrito Flotante y Modal de Pago: Desglose en vivo de cantidades, cálculo de impuestos, validación en tiempo real de números de tarjeta bancaria, fecha de expiración (MM/AA) y CVV, con botón de cierre obvio en la esquina superior derecha.\n"
        "• Feria Virtual (FairPage): Exhibición de puestos comerciales con pabellones temáticos, banners con el color personalizado del toldo elegido por cada comerciante y vitrina interactiva de artículos de feria.\n"
        "• Panel de Mi Stand (MyStandPage): Área de autogestión para comerciantes donde pueden inaugurar su puesto, cambiar color de toldo en vivo, agregar productos a la vitrina o retirarlos.\n"
        "• Gestión de Almacén (InventoryPage): Creación de categorías con control de autoría (bloqueo para usuarios no propietarios), formulario de alta con subida de fotos por archivo o URL, y tabla de almacén con eliminación protegida."
    )

    # =========================================================================
    # 6. INFRAESTRUCTURA Y GUÍA DE DESPLIEGUE EN PRODUCCIÓN
    # =========================================================================
    doc.add_page_break()
    format_heading("6. Infraestructura y Guía de Despliegue en la Nube", level=1)
    
    p = doc.add_paragraph(
        "El sistema está preparado para despliegue contenerizado continuo en servicios PaaS cloud (Render, Vercel, Railway o Kubernetes):"
    )

    format_heading("6.1 Contenerización con Docker Multi-Stage", level=2)
    p = doc.add_paragraph(
        "El repositorio incluye un Dockerfile optimizado en 2 etapas para el backend Spring Boot:\n"
        "1. Etapa de Compilación: Utiliza eclipse-temurin:21-jdk-alpine con Maven wrapper y almacenamiento en caché de dependencias.\n"
        "2. Etapa de Ejecución: Utiliza eclipse-temurin:21-jre-alpine de peso mínimo (~180MB) ejecutando la aplicación con parámetros de memoria y entropía optimizados."
    )

    format_heading("6.2 Variables de Entorno de Configuración", level=2)
    env_data = [
        ["DATABASE_URL", "postgres://user:pass@host:5432/db", "Render / Railway / Supabase", "URL directa de conexión que CloudDataSourceConfig convierte automáticamente a JDBC con SSL."],
        ["PORT", "8080 (o dinámico inyectado por el host)", "PaaS Host", "Puerto TCP donde el servidor Tomcat interno escuchará el tráfico."],
        ["SPRING_DATASOURCE_URL", "jdbc:postgresql://host:5432/vaazar", "Custom Cloud / Local", "URL canónica JDBC de conexión a PostgreSQL."],
        ["VITE_API_URL", "https://vazzar-backend.onrender.com", "Frontend (Vite)", "Dirección base de la API consumida por el frontend en producción."]
    ]
    add_styled_table(["Variable", "Ejemplo de Valor", "Ámbito", "Propósito en Producción"], env_data, col_widths=[1.6, 2.0, 1.2, 1.7])

    format_heading("6.3 Respaldo y Auto-Reparación de Migraciones", level=2)
    p = doc.add_paragraph(
        "Para evitar bloqueos durante despliegues en bases de datos cloud nuevas o tras fallos de red, se implementó "
        "Flyway Repair Post-Processor. Este interceptor ejecuta flyway.repair() de forma preventiva antes de invocar flyway.migrate(), "
        "limpiando automáticamente registros corruptos en flyway_schema_history sin requerir intervención manual vía consola SQL."
    )

    add_callout(
        "Ambos repositorios se encuentran conectados e integrados mediante Continuous Deployment (CD). "
        "Cualquier cambio enviado a la rama 'main' dispara automáticamente el empaquetado y actualización sin tiempo de inactividad.",
        title="CI/CD AUTOMATIZADO"
    )

    # =========================================================================
    # GUARDADO DEL ARCHIVO
    # =========================================================================
    output_path = r"C:\Users\harol\Desktop\Proyectos\Vazzar\DOCUMENTO_TECNICO_VAZZAR.docx"
    doc.save(output_path)
    print(f"Documento guardado con éxito en: {output_path}")

    # Copia en el escritorio para acceso instantáneo
    desktop_path = r"C:\Users\harol\Desktop\DOCUMENTO_TECNICO_VAZZAR.docx"
    doc.save(desktop_path)
    print(f"Copia en escritorio guardada en: {desktop_path}")

if __name__ == "__main__":
    create_document()
