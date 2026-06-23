# Instructions.md - Fixes and Progress

## Progress Made

1. **Command prefix color changed to orange/gold**
   - `ChatInputSuggestorMixin.java`: Added `PREFIX_STYLE = Style.EMPTY.withColor(Formatting.GOLD)` to make the command prefix appear orange in chat input suggestions.

2. **Created `!baritone` command as API endpoint**
   - New file: `src/main/java/adris/rinforzando/commands/BaritoneCommand.java`
   - This command proxies all baritone commands directly through `!baritone [baritone-command]`
   - Uses a custom `RestArg` inner class to capture the entire remaining input as a single string
   - Registered in `RinforzandoCommands.java`

3. **Removed `#` prefix handling for baritone**
   - Baritone normally uses `#` as its command prefix (e.g., `#goal 100 200`)
   - `#` prefix handling was removed from `ChatInputSuggestorMixin.java`
   - `#` prefix handling was removed from `Rinforzando.java` SendChatEvent handler
   - Baritone is now only accessible through `!baritone [baritone commands]`

---

## Critical Bug to Fix

### Crash Report
```
java.lang.StringIndexOutOfBoundsException: Range [0, 10) out of bounds for length 9
at knot//net.minecraft.class_4717.addStyledText(class_4717.java:1087)
at knot//net.minecraft.class_4717.getText(class_4717.java:1272)
at knot//net.minecraft.class_4717.highlightText(class_4717.java:1170)
at knot//net.minecraft.class_4717.handler$zbc000$rinforzando$inj(class_4717.java:1111)
```

### Root Cause
In `ChatInputSuggestorMixin.java`, the `getText` method incorrectly handles the `original` variable when a command prefix is present:

```java
if (hasPrefix) {
    styledText.add(new Pair<>(prefix, PREFIX_STYLE));
    s = s.substring(prefix.length());
    original = s;  // BUG: This changes original to the prefix-stripped version
}

s = addStyledText(styledText, original, s, this.INFO_STYLE, reader);
```

The `reader` was created from the original string **with** prefix, so `reader.getIndex()` still points to positions in the ORIGINAL (longer) string. By changing `original = s`, both `original` and `currentStr` become the shorter (prefix-stripped) string. The `diff` becomes 0, and `index = reader.getIndex()` can exceed `currentStr.length()`, causing `StringIndexOutOfBoundsException`.

### Fix
In `ChatInputSuggestorMixin.java`, at the `getText` method around line 266, change:

```java
if (hasPrefix) {
    styledText.add(new Pair<>(prefix, PREFIX_STYLE));
    s = s.substring(prefix.length());
    original = s;  // REMOVE THIS LINE
}

s = addStyledText(styledText, original, s, this.INFO_STYLE, reader);
```

To:

```java
if (hasPrefix) {
    styledText.add(new Pair<>(prefix, PREFIX_STYLE));
    s = s.substring(prefix.length());
    // original stays as original with prefix intact
}

s = addStyledText(styledText, original, s, this.INFO_STYLE, reader);
```

This ensures `diff = original.length() - currentStr.length()` correctly equals the prefix length, and `index = reader.getIndex() - diff` maps correctly to the prefix-stripped string.

---

## Summary of Changes

| File | Change |
|------|--------|
| `src/main/java/adris/rinforzando/commands/BaritoneCommand.java` | **CREATED** - New baritone proxy command |
| `src/main/java/adris/rinforzando/RinforzandoCommands.java` | Added `BaritoneCommand` registration |
| `src/main/java/adris/rinforzando/mixins/ChatInputSuggestorMixin.java` | Added `PREFIX_STYLE` (gold/orange), prefix styling logic, and `#` prefix removal |
| `src/main/java/adris/rinforzando/Rinforzando.java` | Removed `#` prefix handling from `SendChatEvent` |

## Server Log Summary
- Server crashed with `StringIndexOutOfBoundsException` during chat screen render
- Crash occurred after user typed `r` in baritone chat (baritone responded "Not paused")
- The crash is isolated to the `ChatInputSuggestorMixin.java` styling logic when processing commands with a prefix
- Fix: remove `original = s;` line in the `hasPrefix` block of `getText()`
