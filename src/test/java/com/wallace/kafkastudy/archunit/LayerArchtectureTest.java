package com.wallace.kafkastudy.archunit;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import static com.tngtech.archunit.library.Architectures.layeredArchitecture;

@AnalyzeClasses(packages = "com.wallace", importOptions = {
        ImportOption.DoNotIncludeArchives.class,
        ImportOption.DoNotIncludeJars.class,
        ImportOption.DoNotIncludeTests.class
})
public class LayerArchtectureTest {

    private static final String DOMAIN_LAYER_PACKAGES = "com.wallace.kafkastudy.domain..";
    private static final String APPLICATION_LAYER_PACKAGES = "com.wallace.kafkastudy.application..";
    private static final String INFRASTRUCTURE_LAYER_PACKAGES = "com.wallace.kafkastudy.infrastructure..";

    /*
     * To see more about it Layer ArchTest
     * https://www.archunit.org/userguide/html/000_Index.html#_layer_checks
     */
    @ArchTest
    static ArchRule layered_test = layeredArchitecture().consideringAllDependencies()
            .layer(DOMAIN_LAYER_PACKAGES).definedBy(DOMAIN_LAYER_PACKAGES)
            .layer(APPLICATION_LAYER_PACKAGES).definedBy(APPLICATION_LAYER_PACKAGES)
            .layer(INFRASTRUCTURE_LAYER_PACKAGES).definedBy(INFRASTRUCTURE_LAYER_PACKAGES)
            .whereLayer(DOMAIN_LAYER_PACKAGES).mayNotBeAccessedByAnyLayer()
            .whereLayer(APPLICATION_LAYER_PACKAGES).mayOnlyBeAccessedByLayers(DOMAIN_LAYER_PACKAGES)
            .whereLayer(INFRASTRUCTURE_LAYER_PACKAGES).mayOnlyBeAccessedByLayers(DOMAIN_LAYER_PACKAGES, APPLICATION_LAYER_PACKAGES);
}
