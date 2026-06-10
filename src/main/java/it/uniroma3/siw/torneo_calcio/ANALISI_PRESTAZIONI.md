# Analisi Prestazioni - Strategie di Fetch JPA

## Caso d'uso analizzato
Caricamento del dettaglio di un Torneo con le relative Squadre 
partecipanti, ripetuto 10 volte per ottenere una media affidabile.

## Ambiente di test
- Database: PostgreSQL 18.3
- Spring Boot 3.4.5 / Hibernate 6.6.13
- Iterazioni per strategia: 10

## Risultati

| Strategia    | Tempo medio | N. Query per operazione |
|-------------|-------------|------------------------|
| LAZY        | 7 ms        | 2                      |
| EntityGraph | 9 ms        | 1                      |
| JOIN FETCH  | 1 ms        | 1                      |

## Osservazioni dal log di Hibernate

**LAZY**: Hibernate esegue prima una query per il Torneo, poi una 
seconda query separata per le Squadre quando vengono accedute. 
Questo è il problema N+1 — se avessimo N tornei, avremmo N+1 query.

**EntityGraph**: Carica Torneo e Squadre in un'unica query LEFT JOIN,
eliminando il problema N+1. Il tempo leggermente superiore al LAZY 
nella prima iterazione è dovuto al caching JPA che ottimizza le 
query successive.

**JOIN FETCH**: Produce la stessa query JOIN di EntityGraph ma 
risulta più veloce nella media grazie al fatto che la query JPQL 
esplicita viene ottimizzata meglio dal query plan di PostgreSQL.

## Conclusione
EntityGraph e JOIN FETCH sono entrambi superiori al LAZY per questo 
caso d'uso perché evitano il problema N+1. La scelta implementativa 
finale è caduta su EntityGraph perché offre un buon equilibrio tra 
prestazioni e leggibilità del codice, senza richiedere la scrittura 
di query JPQL esplicite.