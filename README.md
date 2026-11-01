# Coffee-biscuit

A Java library to read Final Fanatasy XIV's game data.

> [!CAUTION]
> This library is highly experimental. A lot of features are yet to be implemented and some features might misbehave.
> If your project allows it, I strongly suggest using the much more battle-tested
> [Lumina](https://github.com/NotAdam/Lumina/). However, if your project *must* be written in java or you would like to
> contribute to this library, any help will be appreciated!

## Features / Roadmap

- [x] Reading raw SqPack files
- [ ] Reading Excel sheets
- [ ] Reading common SqPack file types

## Philosophy

While not the primary objective, this library tries to keep performance in mind when implementing features. For example:
We make use of the Foreign Function and Memory API in order to avoid extra data copying.

Despite this, Java itself imposes restrictions and is quite hostile towards lower level programming. For example, Java
does not have unsigned data types. Therefore, a `long` must be used to contain a `uint32`. Java does not have a `struct`
type that can be directly mapped on memory. Copying must be done in order to work with Java classes and records.

## License

The Unlicense

## Special Thanks

This would not have been possible without relying heavily on the prior works
of [Lumina](https://github.com/NotAdam/Lumina/), The [Dalamud](https://github.com/goatcorp/Dalamud) team and the
[xiv dev](https://xiv.dev/) documentation.
