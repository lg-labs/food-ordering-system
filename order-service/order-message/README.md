# Message Module

## Publisher

First, You must create or have a output port in the application service layer. So, You create an adapter for implement
these port.

```mermaid
flowchart LR
   subgraph application service
       output_port
   end
   subgraph messaging module
     secondary_adapter
   end
    output_port --> secondary_adapter

```