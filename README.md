# VillagerInv

A PaperMC plugin for Minecraft **1.21** that lets permitted players inspect and edit the inventories
of villagers.

Villagers normally carry items in a hidden 8-slot inventory (used for picking up food, seeds and
willingness items for breeding). VillagerInv exposes that inventory through a familiar chest-style
GUI so permitted players can audit and adjust what a villager is holding.

## Features

- Toggleable "open inventory" mode — right-clicking a villager opens its inventory instead of the
  trading menu.
- Chest-style GUI (9 slots, last slot is a barrier filler) that proxies the villager's 8-slot
  inventory.
- Two-way live sync: changes made in the GUI are written back to the villager, and items the
  villager picks up while the GUI is open appear in the view immediately.
- Supports multiple players viewing the same villager simultaneously.
- Optional setting to make villagers drop their inventory contents on death.
- Configurable language (English by default), with all UI strings served through a message service.
- Fine-grained permissions for opening and editing.
- Fully supports modern MiniMessage Format

## Requirements

- Java 21
- PaperMC (or a compatible fork such as Purpur) on Minecraft **1.21**

## Installation

1. Download the plugin release
2. Copy the file `VillagerInv-1.0.0.jar` into your server's `plugins/` directory.
3. Start the server once to generate `plugins/VillagerInv/config.yml`.

## Usage

1. Make sure you have the `villagerinv.use` permission.
2. Run `/villagerinv` (alias: `/npcinv`) to toggle open-inventory mode on.
3. Right-click any villager — the inventory GUI opens.
4. Move items in and out as you would with a chest. Changes are saved automatically.
5. Run `/villagerinv` again to leave open-inventory mode and resume normal trading.

The 9th slot is filled with a barrier item and cannot be interacted with — the underlying villager
inventory only has 8 real slots.

## Commands

| Command        | Alias     | Permission        | Description                                  |
|----------------|-----------|-------------------|----------------------------------------------|
| `/villagerinv` | `/npcinv` | `villagerinv.use` | Toggles villager inventory open mode on/off. |

## Permissions

| Node               | Default | Description                                                     |
|--------------------|---------|-----------------------------------------------------------------|
| `villagerinv.use`  | `op`    | Allows running `/villagerinv` and opening villager inventories. |
| `villagerinv.edit` | `op`    | Allows modifying the contents of a villager's inventory.        |
| `villagerinv.all`  | `op`    | Grants both `villagerinv.use` and `villagerinv.edit`.           |

Players with `villagerinv.use` but without `villagerinv.edit` can open and view inventories but
cannot move items in or out.

## Configuration

`plugins/VillagerInv/config.yml`:

```yaml
# Language tag used to pick a messages_<tag>.properties file (falls back to English).
language: en

# When true, villagers drop everything in their inventory upon death.
villager-drop-inventory-on-death: false
```

## Building from Source

```bash
mvn clean package
```

The shaded jar will be produced in `target/VillagerInv-1.0.0.jar`.

## Authors

- Serge Humphrey (original)
- Whitescan (contributor)