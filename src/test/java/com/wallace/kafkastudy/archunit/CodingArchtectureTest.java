package com.wallace.kafkastudy.archunit;
import com.tngtech.archunit.base.DescribedPredicate;
import com.tngtech.archunit.core.domain.JavaAccess;
import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchIgnore;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchCondition;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.CompositeArchRule;
import com.tngtech.archunit.lang.ConditionEvents;
import com.tngtech.archunit.lang.SimpleConditionEvent;
import org.slf4j.Logger;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.fields;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;
import static com.tngtech.archunit.library.GeneralCodingRules.ACCESS_STANDARD_STREAMS;
import static com.tngtech.archunit.library.GeneralCodingRules.NO_CLASSES_SHOULD_ACCESS_STANDARD_STREAMS;
import static com.tngtech.archunit.library.GeneralCodingRules.NO_CLASSES_SHOULD_THROW_GENERIC_EXCEPTIONS;
import static com.tngtech.archunit.library.GeneralCodingRules.NO_CLASSES_SHOULD_USE_FIELD_INJECTION;
import static com.tngtech.archunit.library.GeneralCodingRules.NO_CLASSES_SHOULD_USE_JAVA_UTIL_LOGGING;

@AnalyzeClasses(packages = "com.wallace", importOptions = {
        ImportOption.DoNotIncludeArchives.class,
        ImportOption.DoNotIncludeJars.class,
        ImportOption.DoNotIncludeTests.class
})
@ArchIgnore
public class CodingArchtectureTest {

    static final List<String> ANNOTATIONS_IGNORED = List.of("Scheduled", "Listener");
    static final List<String> NAME_IGNORED = List.of("Mapper", "Listener", "MapperImpl", "UseCase", "Impl");

    @ArchTest
    private final ArchRule noClassesShouldAccessStandardStreams = NO_CLASSES_SHOULD_ACCESS_STANDARD_STREAMS;

    @ArchTest
    private void setNoClassesShouldAccessStandardStreams(JavaClasses classes) {
        noClasses().should(ACCESS_STANDARD_STREAMS).check(classes);
    }

    @ArchTest
    private final ArchRule noClassesShouldThrowGenericExceptions = NO_CLASSES_SHOULD_THROW_GENERIC_EXCEPTIONS;

    @ArchTest
    private final ArchRule noClassesShouldUseJavaUtilLogging = NO_CLASSES_SHOULD_USE_JAVA_UTIL_LOGGING;

    @ArchTest
    private final ArchRule allClassesShouldUseSLF4JLoggerStaticFinal =
            fields().that().haveRawType(Logger.class)
                    .should().bePrivate()
                    .andShould().beStatic()
                    .andShould().beFinal()
                    .because("All classes should use SLF4J Logger as static final");

    @ArchTest
    private final ArchRule allClassesShouldUseLoggerImportFromSLF4J =
            fields().that()
                    .haveRawType(org.apache.logging.log4j.Logger.class).or().haveRawType(java.util.logging.Logger.class)
                    .should()
                    .notHaveRawType(org.apache.logging.log4j.Logger.class).andShould().notHaveRawType(java.util.logging.Logger.class)
                    .because("All classes should use Logger import from SLF4J")
                    .allowEmptyShould(true);

    @ArchTest
    private final ArchRule noClassesShouldUseFieldInjection = NO_CLASSES_SHOULD_USE_FIELD_INJECTION;

    @ArchTest
    private final ArchRule noClassesShouldUseLombokExperimental = noClasses()
            .should().dependOnClassesThat().resideInAPackage("lombok.experimental..");

    @ArchTest
    private final ArchRule noClassesOnOnCorePackageNotDependsFromLombok =
            noClasses().that().resideInAnyPackage("com.wallace.kafkastudy.domain..")
                    .should().dependOnClassesThat().resideInAnyPackage("lombok..");

    @ArchTest
    static final ArchRule noClassesShouldThrowGenericException =
            CompositeArchRule.of(NO_CLASSES_SHOULD_ACCESS_STANDARD_STREAMS)
                    .and(NO_CLASSES_SHOULD_THROW_GENERIC_EXCEPTIONS);


    static DescribedPredicate<JavaClass> classHasMethodWithAnnotationThatEndsWith() {
        return DescribedPredicate.describe("contains unused method like Scheduler or Listener",
                clazz -> clazz.getMethods().stream()
                        .flatMap(method -> method.getAnnotations().stream())
                        .anyMatch(annotation -> endsWithInsideList(annotation.getRawType().getFullName(), ANNOTATIONS_IGNORED)));
    }

    static DescribedPredicate<JavaClass> classHasNameWithNameThatEndsWith() {
        return DescribedPredicate.describe("class should not be ignored",
                clazz -> endsWithInsideList(clazz.getName(), NAME_IGNORED));
    }

    static boolean endsWithInsideList(String fullName, List<String> ignoredList) {
        return ignoredList
                .stream()
                .anyMatch(fullName::endsWith);
    }

    static class ReferenceValidation extends ArchCondition<JavaClass> {

        public ReferenceValidation(String description) {
            super(description);
        }

        @Override
        public void check(JavaClass javaClass, ConditionEvents events) {
            Set<JavaAccess<?>> accesses = new HashSet<>(javaClass.getAccessesToSelf());
            accesses.removeAll(javaClass.getAccessesFromSelf());
            if (accesses.isEmpty() && javaClass.getDirectDependenciesToSelf().isEmpty()) {
                events.add(new SimpleConditionEvent(javaClass, false, String.format("%s is not referenced in %s",
                        javaClass.getDescription(), javaClass.getSourceCodeLocation())));
            }
        }

    }

}

