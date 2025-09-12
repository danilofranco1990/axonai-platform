package com.axonai.platform.identity.architecture;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.lang.ArchRule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Validação da Arquitetura Hexagonal")
class HexagonalArchitectureTest {

    private JavaClasses importedClasses;

    @BeforeEach
    void setUp() {
        // Importa todas as classes do nosso projeto para serem analisadas pelo ArchUnit.
        // Isso é feito uma vez para otimizar a execução dos testes.
        importedClasses = new ClassFileImporter().importPackages("com.axonai.platform");
    }

    @Test
    @DisplayName("Domínio não deve depender de outras camadas")
    void domain_should_not_depend_on_other_layers() {
        // REGRA: Classes no pacote 'domain' só podem depender de outras classes
        // do 'domain', de bibliotecas padrão do Java ou de algumas libs aprovadas.
        ArchRule rule =
                noClasses()
                        .that()
                        .resideInAPackage("..domain..")
                        .should()
                        .dependOnClassesThat()
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
        ArchRule rule =
                noClasses()
                        .that()
                        .resideInAPackage("..application..")
                        .should()
                        .dependOnClassesThat()
                        .resideInAPackage("..adapter..");

        rule.check(importedClasses);
    }

    @Test
    @DisplayName("Adaptadores de entrada não devem depender de adaptadores de saída")
    void in_adapters_should_not_depend_on_out_adapters() {
        // REGRA: Um adaptador de entrada (ex: web) não deve conhecer e depender
        // diretamente de um adaptador de saída (ex: persistência).
        // A comunicação deve ser mediada pela camada de aplicação (ports).
        ArchRule rule =
                noClasses()
                        .that()
                        .resideInAPackage("..adapter.in..")
                        .should()
                        .dependOnClassesThat()
                        .resideInAPackage("..adapter.out..");

        rule.check(importedClasses);
    }
}
