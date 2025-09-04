package com.axonai.platform.architecture;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.lang.ArchRule;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.stereotype.Service;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

@DisplayName("Validação da Arquitetura Hexagonal")
class HexagonalArchitectureTest {

    private JavaClasses importedClasses;

    @BeforeEach
    void setUp() {
        // Importa todas as classes do nosso projeto para serem analisadas pelo ArchUnit.
        // Isso é feito uma vez para otimizar a execução dos testes.
        importedClasses = new ClassFileImporter()
                .importPackages("com.axonal.platform");
    }

    @Test
    @DisplayName("Domínio não deve depender de outras camadas")
    void domain_should_not_depend_on_other_layers() {
        // REGRA: Classes no pacote 'domain' só podem depender de outras classes
        // do 'domain', de bibliotecas padrão do Java ou de algumas libs aprovadas.
        ArchRule rule = noClasses()
                .that().resideInAPackage("..domain..")
                .should().dependOnClassesThat()
                .resideInAnyPackage("..application..", "..adapter..");

        // Valida a regra contra as classes importadas.
        rule.check(importedClasses);
    }

    @Test
    @DisplayName("Aplicação não deve depender dos adaptadores")
    void application_should_not_depend_on_adapters() {
        // REGRA: A camada de aplicação (incluindo os ports) não pode depender
        // diretamente da camada de adaptadores (infraestrutura).
        // Ela depende de interfaces (ports), não de implementações.
        ArchRule rule = noClasses()
                .that().resideInAPackage("..application..")
                .should().dependOnClassesThat()
                .resideInAPackage("..adapter..");

        rule.check(importedClasses);
    }

    @Test
    @DisplayName("Adaptadores de entrada não devem depender de adaptadores de saída")
    void in_adapters_should_not_depend_on_out_adapters() {
        // REGRA: Um adaptador de entrada (ex: web) não deve conhecer e depender
        // diretamente de um adaptador de saída (ex: persistência).
        // A comunicação deve ser mediada pela camada de aplicação (ports).
        ArchRule rule = noClasses()
                .that().resideInAPackage("..adapter.in..")
                .should().dependOnClassesThat()
                .resideInAPackage("..adapter.out..");

        rule.check(importedClasses);
    }

    @Test
    @DisplayName("Domínio só deve depender de si mesmo e de bibliotecas aprovadas")
    void domain_should_only_depend_on_approved_libraries() {
        // REGRA (REFINADA): É uma regra de "lista branca". O domínio só pode acessar
        // classes de si mesmo, do Java, e de algumas poucas bibliotecas de anotações.
        ArchRule rule = classes()
                .that().resideInAPackage("..domain..")
                .should().onlyDependOnClassesThat()
                .resideInAnyPackage(
                        "com.axonal.platform.domain..",
                        "java..",
                        "jakarta.validation..",
                        "lombok.."
                );

        rule.check(importedClasses);
    }

    // --- REGRAS DE CONVENÇÃO E CODIFICAÇÃO ---

    @Test
    @DisplayName("Serviços de aplicação devem ter o sufixo 'Service'")
    void application_services_should_be_named_correctly() {
        ArchRule rule = classes()
                .that().resideInAPackage("..application.service..")
                .and().areAnnotatedWith(Service.class)
                .should().haveSimpleNameEndingWith("Service");

        rule.check(importedClasses);
    }

    @Test
    @DisplayName("Anotação @Transactional só deve ser usada nos serviços da aplicação")
    void transactional_annotation_should_only_be_used_on_application_services() {
        ArchRule rule = classes()
                .that().areAnnotatedWith(Transactional.class)
                .should().resideInAPackage("..application.service..");

        rule.check(importedClasses);
    }

}