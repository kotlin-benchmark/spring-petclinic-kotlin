CWE-22
Example 1
Source:
[PetController.kt:104](E:/DefensePoint/benchmarks/kotlin-benchmark/spring-petclinic-kotlin/src/main/kotlin/org/springframework/samples/petclinic/owner/PetController.kt#L104)

step 1:
[Owner.kt:99](E:/DefensePoint/benchmarks/kotlin-benchmark/spring-petclinic-kotlin/src/main/kotlin/org/springframework/samples/petclinic/owner/Owner.kt#L99)

step 2:
[Pet.kt:68](E:/DefensePoint/benchmarks/kotlin-benchmark/spring-petclinic-kotlin/src/main/kotlin/org/springframework/samples/petclinic/owner/Pet.kt#L68)

step 3:
[Pet.kt:75](E:/DefensePoint/benchmarks/kotlin-benchmark/spring-petclinic-kotlin/src/main/kotlin/org/springframework/samples/petclinic/owner/Pet.kt#L75)

step 4:
[NamedEntity.kt:44](E:/DefensePoint/benchmarks/kotlin-benchmark/spring-petclinic-kotlin/src/main/kotlin/org/springframework/samples/petclinic/model/NamedEntity.kt#L44)

step 5:
[RecordArchiveSupport.kt:41](E:/DefensePoint/benchmarks/kotlin-benchmark/spring-petclinic-kotlin/src/main/kotlin/org/springframework/samples/petclinic/owner/RecordArchiveSupport.kt#L41)

step 6:
[RecordArchiveSupport.kt:45](E:/DefensePoint/benchmarks/kotlin-benchmark/spring-petclinic-kotlin/src/main/kotlin/org/springframework/samples/petclinic/owner/RecordArchiveSupport.kt#L45)

Sink:
[RecordArchiveSupport.kt:53](E:/DefensePoint/benchmarks/kotlin-benchmark/spring-petclinic-kotlin/src/main/kotlin/org/springframework/samples/petclinic/owner/RecordArchiveSupport.kt#L53)

CWE-327
Example 1
Source/Sink:
[PetController.kt:123](E:/DefensePoint/benchmarks/kotlin-benchmark/spring-petclinic-kotlin/src/main/kotlin/org/springframework/samples/petclinic/owner/PetController.kt#L123)

Note: secondary unlabeled weakness co-located at PetController.kt:119 - System.getProperty("petclinic.archive.releaseKey", "clinicv1") supplies a hardcoded default key and the property is set nowhere in the repo, so the shipped default key is a known constant (CWE-321/CWE-798). Not a planted finding; recorded so a SAST report on that line is not miscounted as a false positive.

CWE-502
Example 1
Source:
[OwnerController.kt:139](E:/DefensePoint/benchmarks/kotlin-benchmark/spring-petclinic-kotlin/src/main/kotlin/org/springframework/samples/petclinic/owner/OwnerController.kt#L139)

step 1:
[Owner.kt:109](E:/DefensePoint/benchmarks/kotlin-benchmark/spring-petclinic-kotlin/src/main/kotlin/org/springframework/samples/petclinic/owner/Owner.kt#L109)

step 2:
[Owner.kt:114](E:/DefensePoint/benchmarks/kotlin-benchmark/spring-petclinic-kotlin/src/main/kotlin/org/springframework/samples/petclinic/owner/Owner.kt#L114)

step 3:
[Person.kt:45](E:/DefensePoint/benchmarks/kotlin-benchmark/spring-petclinic-kotlin/src/main/kotlin/org/springframework/samples/petclinic/model/Person.kt#L45)

step 4:
[RecordArchiveSupport.kt:66](E:/DefensePoint/benchmarks/kotlin-benchmark/spring-petclinic-kotlin/src/main/kotlin/org/springframework/samples/petclinic/owner/RecordArchiveSupport.kt#L66)

step 5:
[RecordArchiveSupport.kt:71](E:/DefensePoint/benchmarks/kotlin-benchmark/spring-petclinic-kotlin/src/main/kotlin/org/springframework/samples/petclinic/owner/RecordArchiveSupport.kt#L71)

step 6:
[RecordArchiveSupport.kt:76](E:/DefensePoint/benchmarks/kotlin-benchmark/spring-petclinic-kotlin/src/main/kotlin/org/springframework/samples/petclinic/owner/RecordArchiveSupport.kt#L76)

Sink:
[RecordArchiveSupport.kt:84](E:/DefensePoint/benchmarks/kotlin-benchmark/spring-petclinic-kotlin/src/main/kotlin/org/springframework/samples/petclinic/owner/RecordArchiveSupport.kt#L84)

CWE-328
Example 1
Source/Sink:
[OwnerController.kt:176](E:/DefensePoint/benchmarks/kotlin-benchmark/spring-petclinic-kotlin/src/main/kotlin/org/springframework/samples/petclinic/owner/OwnerController.kt#L176)

CWE-22
Example 2
Source:
[VisitController.kt:82](E:/DefensePoint/benchmarks/kotlin-benchmark/spring-petclinic-kotlin/src/main/kotlin/org/springframework/samples/petclinic/owner/VisitController.kt#L82)

step 1:
[Visit.kt:60](E:/DefensePoint/benchmarks/kotlin-benchmark/spring-petclinic-kotlin/src/main/kotlin/org/springframework/samples/petclinic/visit/Visit.kt#L60)

step 2:
[Pet.kt:82](E:/DefensePoint/benchmarks/kotlin-benchmark/spring-petclinic-kotlin/src/main/kotlin/org/springframework/samples/petclinic/owner/Pet.kt#L82)

step 3:
[BaseEntity.kt:47](E:/DefensePoint/benchmarks/kotlin-benchmark/spring-petclinic-kotlin/src/main/kotlin/org/springframework/samples/petclinic/model/BaseEntity.kt#L47)

step 4:
[VisitReportStore.kt:39](E:/DefensePoint/benchmarks/kotlin-benchmark/spring-petclinic-kotlin/src/main/kotlin/org/springframework/samples/petclinic/visit/VisitReportStore.kt#L39)

step 5:
[VisitReportStore.kt:51](E:/DefensePoint/benchmarks/kotlin-benchmark/spring-petclinic-kotlin/src/main/kotlin/org/springframework/samples/petclinic/visit/VisitReportStore.kt#L51)

step 6:
[VisitReportStore.kt:55](E:/DefensePoint/benchmarks/kotlin-benchmark/spring-petclinic-kotlin/src/main/kotlin/org/springframework/samples/petclinic/visit/VisitReportStore.kt#L55)

Sink:
[VisitReportStore.kt:63](E:/DefensePoint/benchmarks/kotlin-benchmark/spring-petclinic-kotlin/src/main/kotlin/org/springframework/samples/petclinic/visit/VisitReportStore.kt#L63)

CWE-502
Example 2
Source:
[VisitController.kt:92](E:/DefensePoint/benchmarks/kotlin-benchmark/spring-petclinic-kotlin/src/main/kotlin/org/springframework/samples/petclinic/owner/VisitController.kt#L92)

step 1:
[Visit.kt:66](E:/DefensePoint/benchmarks/kotlin-benchmark/spring-petclinic-kotlin/src/main/kotlin/org/springframework/samples/petclinic/visit/Visit.kt#L66)

step 2:
[Pet.kt:89](E:/DefensePoint/benchmarks/kotlin-benchmark/spring-petclinic-kotlin/src/main/kotlin/org/springframework/samples/petclinic/owner/Pet.kt#L89)

step 3:
[Pet.kt:96](E:/DefensePoint/benchmarks/kotlin-benchmark/spring-petclinic-kotlin/src/main/kotlin/org/springframework/samples/petclinic/owner/Pet.kt#L96)

step 4:
[BaseEntity.kt:54](E:/DefensePoint/benchmarks/kotlin-benchmark/spring-petclinic-kotlin/src/main/kotlin/org/springframework/samples/petclinic/model/BaseEntity.kt#L54)

step 5:
[VisitReportStore.kt:73](E:/DefensePoint/benchmarks/kotlin-benchmark/spring-petclinic-kotlin/src/main/kotlin/org/springframework/samples/petclinic/visit/VisitReportStore.kt#L73)

step 6:
[VisitReportStore.kt:82](E:/DefensePoint/benchmarks/kotlin-benchmark/spring-petclinic-kotlin/src/main/kotlin/org/springframework/samples/petclinic/visit/VisitReportStore.kt#L82)

Sink:
[VisitReportStore.kt:91](E:/DefensePoint/benchmarks/kotlin-benchmark/spring-petclinic-kotlin/src/main/kotlin/org/springframework/samples/petclinic/visit/VisitReportStore.kt#L91)

CWE-295
Example 1
Source/Sink:
[VetController.kt:62](E:/DefensePoint/benchmarks/kotlin-benchmark/spring-petclinic-kotlin/src/main/kotlin/org/springframework/samples/petclinic/vet/VetController.kt#L62)

Note: engineered host: added a com.squareup.okhttp3:okhttp:4.12.0 dependency + an outbound "specialty registry" sync client on a new GET /vets/registry/refresh route (project had no outbound HTTPS surface).

CWE-798
Example 1
Source/Sink:
[VetController.kt:72](E:/DefensePoint/benchmarks/kotlin-benchmark/spring-petclinic-kotlin/src/main/kotlin/org/springframework/samples/petclinic/vet/VetController.kt#L72)

Note: engineered host: shares the CWE-295/1 outbound "specialty registry" sync client (GET /vets/registry/refresh). //SOURCE on the hardcoded password val at VetController.kt:67.

CWE-295
Example 2
Source/Sink:
[WelcomeController.kt:41](E:/DefensePoint/benchmarks/kotlin-benchmark/spring-petclinic-kotlin/src/main/kotlin/org/springframework/samples/petclinic/system/WelcomeController.kt#L41)

Note: engineered host: second independent outbound "clinic status feed" client on a new GET /status/sync route (distinct from the CWE-295/1 vet-registry client).

CWE-798
Example 2
Source/Sink:
[WelcomeController.kt:51](E:/DefensePoint/benchmarks/kotlin-benchmark/spring-petclinic-kotlin/src/main/kotlin/org/springframework/samples/petclinic/system/WelcomeController.kt#L51)

Note: engineered host: shares the CWE-295/2 outbound "clinic status feed" client (GET /status/sync). //SOURCE on the hardcoded password val at WelcomeController.kt:46.

CWE-328
Example 2
Source/Sink:
[CrashController.kt:58](E:/DefensePoint/benchmarks/kotlin-benchmark/spring-petclinic-kotlin/src/main/kotlin/org/springframework/samples/petclinic/system/CrashController.kt#L58)

CWE-327
Example 2
Source/Sink:
[CrashController.kt:71](E:/DefensePoint/benchmarks/kotlin-benchmark/spring-petclinic-kotlin/src/main/kotlin/org/springframework/samples/petclinic/system/CrashController.kt#L71)
